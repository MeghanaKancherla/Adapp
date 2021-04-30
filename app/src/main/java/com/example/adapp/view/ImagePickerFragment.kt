package com.example.adapp.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.adapp.R
import com.example.adapp.model.Image
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.fragment_image_picker.*
import java.io.ByteArrayOutputStream
import java.io.IOException


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ImagePickerFragment : Fragment() {

    private val GALLERY : Int = 1
    private  val CAMERA : Int = 2
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var bundle : Bundle? = null
    var imageUri: Uri? = null
    var img = Image()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            bundle = it.getBundle("adDeatils")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //Toast.makeText(activity, "${bundle?.getString("brand")}\n${bundle?.getString("title")}", Toast.LENGTH_LONG).show()
        requestMultiplePermissions()

        uploadImgB.visibility = View.INVISIBLE

        showPictureDialog()

        changeUploadButton?.setOnClickListener({ showPictureDialog() })

        super.onViewCreated(view, savedInstanceState)
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ImagePickerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun showPictureDialog() {

        val pictureDialogItems = arrayOf(
            "Select photo from gallery",
            "Capture photo from camera"
        )
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Select Action")
            .setItems(pictureDialogItems) { dialog, which ->
                // Respond to item chosen
                when (which) {
                    0 -> choosePhotoFromGallary()
                    1 -> takePhotoFromCamera()
                }
            }
            .show()
//
    }
    fun choosePhotoFromGallary() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(galleryIntent, GALLERY)
    }

    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_CANCELED) {
            return
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                val contentURI: Uri? = data.data
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(
                        context?.contentResolver,
                        contentURI
                    )
                    selectedUploadImage!!.setImageBitmap(bitmap)
                    uploadImgB.visibility = View.VISIBLE
                    uploadImgB.setOnClickListener {
                        imageUri = contentURI
                        img.imgUri = imageUri!!
                        nextFragmentNavigation()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        } else if (requestCode == CAMERA) {
            if (data != null) {
                val contentUri: Uri? = data.data
                val thumbnail = data.extras!!["data"] as Bitmap?
                selectedUploadImage!!.setImageBitmap(thumbnail)
                uploadImgB.visibility = View.VISIBLE
                uploadImgB.setOnClickListener {
                    imageUri = getImageUri(requireContext(), thumbnail!!)
                    img.imgUri = imageUri!!
                    nextFragmentNavigation()
                }
            }
        }
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            inContext.getContentResolver(),
            inImage,
            "Title",
            null
        )
        return Uri.parse(path)
    }

    fun nextFragmentNavigation(){
        val imgBundle = Bundle()
        imgBundle.putBundle("adBundle", bundle)
        imgBundle.putSerializable("url", img)
        findNavController().navigate(R.id.action_imagePickerFragment_to_verifyFragment, imgBundle)
    }

    private fun requestMultiplePermissions() {
        Dexter.withActivity(activity)
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    // check if all permissions are granted
                    if (report.areAllPermissionsGranted()) {
                        Toast.makeText(
                            context,
                            "All permissions are granted by user!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            }).withErrorListener { Toast.makeText(context, "Some Error! ", Toast.LENGTH_SHORT).show() }
            .onSameThread()
            .check()
    }



}