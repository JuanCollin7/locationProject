package com.jc.locationproject

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.Location
import android.util.Log
import com.jc.locationproject.LocationService.Companion.LOCATION

class LocationBroadcastReceiver(val onLocationReceived: (Location) -> Unit): BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.getParcelableExtra<Location>(LOCATION)?.let {
            onLocationReceived(it)
        }
    }
}