package com.example.adapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.adapp.view.RegisterFragment
import com.example.adapp.view.SignInFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val signInFragment=SignInFragment()
        supportFragmentManager.beginTransaction().add(R.id.parentL,signInFragment).commit()
    }
}