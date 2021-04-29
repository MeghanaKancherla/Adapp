package com.example.adapp.presenter

import android.net.Uri
import com.example.adapp.model.Advertisement
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class AddPresenter(val view: View) {

    val user = FirebaseAuth.getInstance().currentUser.uid
    val ref = FirebaseStorage.getInstance().getReference("Images")
    val mRef  = FirebaseDatabase.getInstance().getReference("Advertisements")

    fun addAd(ad: Advertisement, uri: Uri){
        ad.uid = user
        if(uri != null) {
            val fileRef = ref.child("${System.currentTimeMillis()}.${view.getFileExtension(uri)}")
            fileRef.putFile(uri)
                    .addOnSuccessListener {
                        fileRef.downloadUrl.addOnSuccessListener {
                            ad.imageUrl = it.toString()
                            val addId = mRef.push().key!!
                            mRef.child(addId).setValue(ad)
                            view.sentToast("Ad is added successfully!")
                            view.stopProgressBar()
                        }
                    }
                    .addOnProgressListener {

                    }
                    .addOnFailureListener {
                        view.sentToast("Uploading Failed")
                        view.stopProgressBar()
                    }
        }
        else{
            view.sentToast("Select an image")
        }
    }

    fun addAdvertisement(ad: Advertisement){
        mRef.push().setValue(ad)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    view.sentToast("Ad is added successfully")
                    view.stopProgressBar()
                }
                else{
                    view.sentToast("Ad is not added")
                    view.stopProgressBar()
                }
            }
    }

    interface View{
        fun sentToast(message: String)
        fun getFileExtension(myUri: Uri): String?
        fun stopProgressBar()
    }
}