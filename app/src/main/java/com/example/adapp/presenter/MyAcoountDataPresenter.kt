package com.example.adapp.presenter

import android.util.Log
import android.widget.Toast
import com.example.adapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class MyAcoountDataPresenter(val view:View) {

    fun getMyAccountData():User?
    {
        var data:User?=null
        val user=FirebaseAuth.getInstance().currentUser
        FirebaseDatabase.getInstance()
            .getReference("Users")
            .child(user.uid)
            .get()
            .addOnSuccessListener{
               val email=it.child("email").value.toString()
                val phoneNumber=it.child("phoneNumber").value.toString()
               val  username=it.child("username").value.toString()
                val password=it.child("password").value.toString()
                data=User(username,email,password,phoneNumber)
                Log.d("MyAccountPresenter","$data")
            }.addOnFailureListener {
                view.sendToast("Failed to get Data..")
            }
        return data

    }
    interface View{
        fun sendToast(message: String)
    }

}