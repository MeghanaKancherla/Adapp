package com.example.adapp.presenter

import android.app.Activity
import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.example.adapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AuthPresenter(val view: View){


    fun createAccount(username: String, email: String, password: String, phoneNo: String):Boolean{
        val fAuth= FirebaseAuth.getInstance()
        var flag=true
        fAuth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener{ task ->
                if(task.isSuccessful)
                {

                    val user=fAuth.currentUser
                    val userObj= User(username,email,password,phoneNo)
                    FirebaseDatabase.getInstance().getReference("Users")
                        .child(user.uid).setValue(userObj)
                }
                else
                {
                    flag=false
                }
            }
        return flag
    }

    fun loginUser(userMail: String, userPassword: String):Boolean{
        val lAuth = FirebaseAuth.getInstance()
        var flag=true
        lAuth.signInWithEmailAndPassword(userMail,userPassword)
            .addOnCompleteListener() { task->
                if(task.isSuccessful)
                {
                    val user = FirebaseAuth.getInstance().currentUser
                    if(user.isEmailVerified)
                    {
                        view.sendToast("Logged In successfully")
                    }
                    else
                    {
                        user.sendEmailVerification()
                        view.sendToast("Verification mail sent..")
                        flag=false
                    }
                }
                else
                {
                    view.sendToast("Failed to login")
                    flag=false
                }
            }
        return flag
    }
    fun updateData(changedPhoneNumber: String, changedUserName: String) {
        val user=FirebaseAuth.getInstance().currentUser
        val uid=user.uid
        val databaseRef=FirebaseDatabase.getInstance()
            .getReference("Users").child(uid)
        databaseRef.child("username").setValue(changedUserName)
        databaseRef.child("phoneNumber").setValue(changedPhoneNumber)
        view.sendToast("Changes made successfully")
        //Toast.makeText(activity,"Changes made successfully",Toast.LENGTH_SHORT).show()
    }

    interface View{
        fun sendToast(message: String)
    }
}