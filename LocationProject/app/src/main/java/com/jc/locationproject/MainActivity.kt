package com.jc.locationproject

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.jc.locationproject.database.AppDatabase
import com.jc.locationproject.database.LocationLog
import com.jc.locationproject.ui.locations.LocationsActivity
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startButton: Button = findViewById(R.id.startButton)

        startButton.setOnClickListener {
            startActivity(LocationsActivity.getInstance(this))
        }

        requestLocationPermission()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun requestLocationPermission() {
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_BACKGROUND_LOCATION, false) -> {
                    Log.v("PERMISSION", "BACKGROUND")
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                } else -> {
                // No location access granted.
            }
            }
        }

        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION))
    }
}