package com.jc.locationproject.services

import android.app.Application
import android.location.Location
import android.util.Log
import com.jc.locationproject.database.AppDatabase
import com.jc.locationproject.database.LocationLog
import java.util.*

class LocationManager(application: Application) {

    private val database by lazy { AppDatabase.getDatabase(application) }

    suspend fun getAll(): List<LocationLog> {
        return database.locationLogDao().getAll()
    }

    suspend fun loadAllByIds(userId: Int): List<LocationLog> {
        return database.locationLogDao().loadAllByIds(userId)
    }

    suspend fun loadLatestById(userId: Int): LocationLog? {
        return database.locationLogDao().loadLatestById(userId)
    }

    suspend fun insert(userId: Int, location: Location) {
        val latestLocationLog = loadLatestById(userId)

        if (latestLocationLog == null) {
            insertNewLog(userId, location)
            return
        }

        if (shouldSaveLocation(latestLocationLog, location)) {
            insertNewLog(userId, location, latestLocationLog)
        }
    }

    private suspend fun insertNewLog(userId: Int, location: Location, previousLog: LocationLog? = null) {
        val timestamp = Calendar.getInstance().time.time

        val newLog = LocationLog(
            timestamp,
            userId,
            location.latitude,
            location.longitude,
            timestamp,
            false,
            0.0,
            0.0,
            0.0)

        previousLog?.let {
            newLog.displacement = getDistance(it, location)
            newLog.velocity = getVelocity(it, location)
            newLog.interval = getInterval(it, location)
        }

        database.locationLogDao().insertAll(newLog)
    }

    suspend fun delete(locationLog: LocationLog) {
        database.locationLogDao().delete(locationLog)
    }

    suspend fun deleteAll() {
        database.locationLogDao().deleteAll()
    }

    private fun shouldSaveLocation(locationLog: LocationLog, location: Location): Boolean {
        val distanceInMeters = getDistance(locationLog, location)

        return distanceInMeters >= MINIMUM_DISTANCE
    }

    // Get the distance in meters between log and location
    private fun getDistance(locationLog: LocationLog, location: Location): Double {
        val locationFromLog = Location("")
        locationFromLog.latitude = locationLog.lat ?: 0.0
        locationFromLog.longitude = locationLog.lon ?: 0.0

        return location.distanceTo(locationFromLog).toDouble()
    }

    // Get the interval in seconds between log and location
    private fun getInterval(locationLog: LocationLog, location: Location): Double {
        locationLog.timestamp?.let {
            val diff = location.time - it
            val minutes = diff.toDouble() / 1000.0
            return minutes
        }

        return 0.0
    }

    // Get the velocity in m/s between log and location
    private fun getVelocity(locationLog: LocationLog, location: Location): Double {
        val minutes = getInterval(locationLog, location)
        val distance = getDistance(locationLog, location)

        return distance / minutes
    }

    companion object {
        // Minimum distance in meters to save new location
        internal const val MINIMUM_DISTANCE = 200.0
    }
}