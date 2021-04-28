package com.example.adapp.presenter

import com.example.adapp.model.Response
import com.example.adapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AccountPresenter(val view: View) {

    lateinit var userAccount: User
    private val rootRef: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val userRef: DatabaseReference = rootRef.child("Users")

    fun getAccountDetails(callback: FirebaseCallback){
        userAccount = User()
        val user = FirebaseAuth.getInstance().currentUser
        userRef.child(user.uid).get().addOnCompleteListener { task ->
            val response = Response()
            if(task.isSuccessful){
                val result = task.result
                result?.let {
                    response.user = it.getValue(User::class.java)!!
                }
            }
            else{
                response.exception = task.exception
            }
            callback.onResponse(response)
        }
    }

    interface View{
        fun sendToast(msg: String)
    }
}