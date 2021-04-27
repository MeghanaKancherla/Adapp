package com.example.adapp.presenter

import android.app.Activity
import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.example.adapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterPresenter(val activity: FragmentActivity){
     fun createAccount(username: String, email: String, password: String, phoneNo: String) {
        val fAuth= FirebaseAuth.getInstance()
        fAuth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(activity){
                    task ->
                if(task.isSuccessful)
                {
                    val user=fAuth.currentUser
                    val userObj= User(username,email,password,phoneNo)
                    FirebaseDatabase.getInstance().getReference("Users")
                        .child(user.uid).setValue(userObj)
                }
                else
                {
                    Toast.makeText(activity,"Unable to complete Registration..", Toast.LENGTH_SHORT).show()
                }

            }
    }
}