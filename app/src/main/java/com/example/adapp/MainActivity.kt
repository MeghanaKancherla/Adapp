package com.example.adapp

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.os.Build
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import android.util.Log
import android.widget.Toast
import com.example.adapp.view.RegisterFragment
import com.example.adapp.view.SignInFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    val PREF_NAME="currentLocation"
    private var lati : Double = 12.973826
    private var longi : Double = 77.590591
    lateinit var lManager: LocationManager
    var providerName=""

    @RequiresApi(Build.VERSION_CODES.M)
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
            finish()
            val intent= Intent(this,Nav_activity::class.java)
            startActivity(intent)

        }

        getLocation()


        val signInFragment=SignInFragment()
        supportFragmentManager.beginTransaction().add(R.id.parentL,signInFragment).commit()

    }


    @RequiresApi(Build.VERSION_CODES.M)
    fun getLocation ( ){
        if (checkPermission()){

            lManager = getSystemService(LOCATION_SERVICE) as LocationManager

            val providerList = lManager.getProviders(true)

            if (providerList.isNotEmpty()) {
                if (providerList.contains(LocationManager.GPS_PROVIDER))
                    providerName = LocationManager.GPS_PROVIDER
                else if (providerList.contains(LocationManager.NETWORK_PROVIDER))
                    providerName = LocationManager.NETWORK_PROVIDER
                else
                    providerName = providerList[0]
            } else
                Toast.makeText(this, " location not traced", Toast.LENGTH_SHORT).show()
            val loc = lManager.getLastKnownLocation(providerName)
            if (loc != null) {
                lati = loc.latitude
                longi = loc.longitude
            }
            getaddress()
        }
    }
    @RequiresApi(Build.VERSION_CODES.M)
    fun checkPermission() : Boolean{
        if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(
                        Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED){
            //ask user to grant
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),1)
            return false

        }
        else {
            // Toast.makeText(this,"All permissions are granted ",Toast.LENGTH_SHORT).show()
            return true
        }
    }
    fun getaddress() {
        val newG = Geocoder(applicationContext)
        val adresses = newG.getFromLocation(lati, longi, 1)
        val address = adresses[0]
        val area = address.getAddressLine(0)
        //Log.d("address", "result : $area")
        val pref =getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        val editor =pref.edit()
        editor.putString("area",area)
        editor.commit()

    }
}

