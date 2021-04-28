package com.example.adapp.presenter

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
                    val user = fAuth.currentUser
                    val userObj = User(username,email,password,phoneNo)
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

    fun loginUser(userMail: String, userPassword: String) {
        val lAuth = FirebaseAuth.getInstance()
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
                    }
                }
                else
                {
                    view.sendToast("Failed to login")
                }
            }
    }


    interface View{
        fun sendToast(message: String)
    }
}