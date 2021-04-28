package com.example.adapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.adapp.view.RegisterFragment
import com.example.adapp.view.SignInFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val currentUser= FirebaseAuth.getInstance().currentUser
        if(currentUser==null)
        {
            val signInFragment=SignInFragment()
            supportFragmentManager.beginTransaction().add(R.id.parentL,signInFragment).commit()

        }
        else
        {
            val intent= Intent(this,Nav_activity::class.java)
            startActivity(intent)

        }

    }
}