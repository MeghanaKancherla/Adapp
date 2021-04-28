package com.example.adapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_nav_activity.*

class Nav_activity : AppCompatActivity() {

    //lateinit var bottomNavigationView : BottomNavigationView
    lateinit var  transaction:FragmentTransaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav_activity)
        bottomNavigationView.background = null
        bottomNavigationView.menu.getItem(2).isEnabled = false

        //initializing all fragments


        //click listener to bottom navigation bar items
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home -> {
                    val allAdsFragment = AllAdsFragment()
                    setCurrentFragment(allAdsFragment) }

                R.id.myAds ->{
                    val myAdsFragment = MyAdsFragment()
                    setCurrentFragment(myAdsFragment)
                }
                R.id.account ->{
                    val myAccountFragment = MyAccountFragment()
                    setCurrentFragment(myAccountFragment)
                }
                R.id.notifications ->{
                    val notificationFragment = NotificationFragment()
                    setCurrentFragment(notificationFragment)
                }
            }
            true
        }

    }
    //common function to set fragment to constraint layout
    private fun setCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.constraintLayout,fragment)
            commit()
        }
    fun addAdvertisement(view:View){
        val newAdFragment = NewAdFragment()
        setCurrentFragment(newAdFragment)
    }
}