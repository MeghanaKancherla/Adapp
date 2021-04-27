package com.example.adapp.presenter

import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.example.adapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterPresenter(val view: View){

    fun createAccount(username: String, email: String, password: String, phoneNo: String) {
        val fAuth= FirebaseAuth.getInstance()
        fAuth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener{ task ->
                if(task.isSuccessful)
                {
                    val user=fAuth.currentUser
                    val userObj= User(username,email,password,phoneNo)
                    FirebaseDatabase.getInstance().getReference("Users")
                        .child(user.uid).setValue(userObj)
                    view.sendToast("Registration is successful")

                }
                else
                {
                    view.sendToast("Unable to complete Registration..")
                }

            }
    }

    interface View{
        fun sendToast(message: String)
    }
}