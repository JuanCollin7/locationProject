package com.jc.locationproject.ui.locations

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.jc.locationproject.R

class LocationsActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_locations)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, LocationsFragment.newInstance())
                .commitNow()
        }

        database = Firebase.database.reference
        database.child("users").child("UID07").setValue("Juan")
    }

    companion object {
        fun getInstance(context: Context) = Intent(context, LocationsActivity::class.java)
    }
}