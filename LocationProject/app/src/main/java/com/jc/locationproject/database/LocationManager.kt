package com.jc.locationproject.database

import android.app.Application
import android.location.Location
import android.util.Log
import java.util.*

class LocationManager(application: Application) {

    private val database by lazy { AppDatabase.getDatabase(application) }

    suspend fun getAll(): List<LocationLog> {
        return database.locationLogDao().getAll()
    }

    suspend fun loadAllByIds(userId: Int): List<LocationLog> {
        return database.locationLogDao().loadAllByIds(userId)
    }

    private suspend fun loadLatestById(userId: Int): LocationLog? {
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

        previousLog.let {
            newLog.displacement = 0.0
            newLog.velocity = 0.0
            newLog.interval = 0.0
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
        return distanceInMeters >= LocationManager.MINIMUM_DISTANCE
    }

    private fun getDistance(locationLog: LocationLog, location: Location): Float {
        val locationFromLog = Location("")
        locationFromLog.latitude = locationLog.lat ?: 0.0
        locationFromLog.longitude = locationLog.lon ?: 0.0

        return location.distanceTo(locationFromLog)
    }

    companion object {
        // Minimum distance in meters to save new location
        internal const val MINIMUM_DISTANCE = 50.0
    }
}