package com.jc.locationproject

import android.Manifest
import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.os.*
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.*
import kotlinx.coroutines.*


class LocationService: Service() {

    private val notificationManager by lazy { getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }

    private lateinit var deviceManager: android.location.LocationManager

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    @SuppressLint("MissingPermission")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        startForeground(NOTIFICATION_ID, generateNotification())

        startCoreLocation()

        return START_NOT_STICKY
    }

    private fun startCoreLocation(){

        deviceManager = getSystemService(Context.LOCATION_SERVICE) as android.location.LocationManager

        val hasFineLocationPermission = ActivityCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        )
        val hasCoarseLocationPermission = ActivityCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if(hasFineLocationPermission == PackageManager.PERMISSION_DENIED || hasCoarseLocationPermission == PackageManager.PERMISSION_DENIED) {
            return
        }

        deviceManager.let { locationManager ->
            locationManager.requestLocationUpdates(
                android.location.LocationManager.GPS_PROVIDER, 5000, 3f, LocationListener {
                    fireNotification(it)
                }
            )
        }
    }

    private fun fireNotification(location: Location) {
        val intent = Intent(ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST)
        intent.putExtra(LOCATION, location)
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
    }

    private fun generateNotification(): Notification {

        val mainNotificationText = "TEXT"
        val titleText = getString(R.string.app_name)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                titleText,
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationChannel.description = "Description"
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val bigTextStyle = NotificationCompat.BigTextStyle()
            .bigText(mainNotificationText)
            .setBigContentTitle(titleText)

        val notificationCompatBuilder =
            NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)

        return notificationCompatBuilder
            .setStyle(bigTextStyle)
            .setContentTitle(titleText)
            .setContentText(mainNotificationText)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setOngoing(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .build()
    }

    companion object {
        internal const val ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST =
            "${BuildConfig.APPLICATION_ID}.action.FOREGROUND_ONLY_LOCATION_BROADCAST"

        internal const val LOCATION =
            "${BuildConfig.APPLICATION_ID}.field.LOCATION"

        private const val NOTIFICATION_ID = 12345678

        private const val NOTIFICATION_CHANNEL_ID = "LOCATION_CHANNEL_ID"
    }
}