package com.example.adapp.view

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import com.example.adapp.R
import com.example.adapp.model.Advertisement
import com.example.adapp.model.Image
import com.example.adapp.model.Response
import com.example.adapp.presenter.AddPresenter
import com.example.adapp.presenter.FirebaseCallback
import com.example.adapp.presenter.MyAcountDataPresenter
import kotlinx.android.synthetic.main.fragment_verify.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [VerifyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class VerifyFragment : Fragment(), FirebaseCallback, MyAcountDataPresenter.View, AddPresenter.View {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var accountPresenter: MyAcountDataPresenter
    var adBundle: Bundle? = null
    lateinit var img: Image
    lateinit var addPresenter: AddPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            adBundle = it.getBundle("adBundle")
            img = it.getSerializable("url") as Image
        }
        accountPresenter = MyAcountDataPresenter(this)
        accountPresenter.getAccountDetails(this)
        addPresenter = AddPresenter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_verify, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postB.setOnClickListener {
            val name = usernameVerifyET.text.toString()
            val phone = contactVerifyET.text.toString()
            val loc = locationVerifyET.text.toString()
            val category = adBundle?.getString("category")
            val brand = adBundle?.getString("brand")
            val title = adBundle?.getString("title")
            val desc = adBundle?.getString("description")
            val price = adBundle?.getString("price")
            val imgUrl = img.imgUri.toString()
            val ad = Advertisement(category, brand, title, desc, price, imgUrl, name, phone, loc)
            pBarVerify.visibility = View.VISIBLE
            addPresenter.addAd(ad, img.imgUri!!)
            //Toast.makeText(activity, "$ad", Toast.LENGTH_LONG).show()
        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment VerifyFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            VerifyFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onResponse(response: Response) {
        usernameVerifyET.setText(response.user?.username)
        contactVerifyET.setText(response.user?.phoneNumber)
        pBarVerify.visibility = View.GONE
    }

    override fun sendToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    override fun sentToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    override fun getFileExtension(myUri: Uri): String? {
        val cr = context?.contentResolver
        val mime: MimeTypeMap = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cr?.getType(myUri))
    }

    override fun stopProgressBar() {
        pBarVerify.visibility = View.GONE
    }
}