package com.example.adapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.adapp.view.RegisterFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val regFrag= RegisterFragment()

        supportFragmentManager.beginTransaction().add(R.id.parentL,regFrag).commit()
    }
}