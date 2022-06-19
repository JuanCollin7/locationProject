package com.jc.locationproject.services

import android.content.Context
import android.location.Location
import android.util.Log
import com.jc.locationproject.database.AppDatabase
import com.jc.locationproject.database.LocationLog
import java.util.*

class LocationManager(context: Context) {

    private val database by lazy { AppDatabase.getDatabase(context) }

    suspend fun getAll(): List<LocationLog> {
        return database.locationLogDao().getAll()
    }

    suspend fun getAllUnsynced(userId: Int): List<LocationLog> {
        return database.locationLogDao().getAllUnsynced(userId)
    }

    suspend fun loadAllById(userId: Int): List<LocationLog> {
        return database.locationLogDao().loadAllById(userId)
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

        if(shouldConsiderAsStopped(latestLocationLog, location)) {
            updateStoppedLog(location, latestLocationLog)
        } else if (shouldSaveLocation(latestLocationLog, location)) {
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
            timestamp,
            false,
            0.0,
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

    private suspend fun updateStoppedLog(location: Location, previousLog: LocationLog) {
        val timestamp = Calendar.getInstance().time.time
        val interval = getInterval(previousLog, location)
        val totalStoppedTime = interval + (previousLog.stoppedTime ?: 0.0)
        database.locationLogDao().update(previousLog.uid, totalStoppedTime, timestamp)
    }

    suspend fun updateSyncedLogs(logs: List<LocationLog>) {
        val ids = logs.map { it.uid }
        database.locationLogDao().update(ids, true)
    }

    suspend fun delete(locationLog: LocationLog) {
        database.locationLogDao().delete(locationLog)
    }

    suspend fun deleteAll() {
        database.locationLogDao().deleteAll()
    }

    suspend fun deleteAllSynced() {
        return database.locationLogDao().deleteAllSynced()
    }

    private fun shouldConsiderAsStopped(locationLog: LocationLog, location: Location): Boolean {
        val distanceInMeters = getDistance(locationLog, location)
        return distanceInMeters <= MAXIMUM_DISTANCE_TO_CONSIDER_AS_STOPPED
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

    // Get the interval in minutes between log and location
    private fun getInterval(locationLog: LocationLog, location: Location): Double {
        locationLog.updatedOn?.let {
            val diff = location.time - it
            return diff.toDouble() / 1000.0
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

        // Maximum distance in meters to consider the device is stopped
        internal const val MAXIMUM_DISTANCE_TO_CONSIDER_AS_STOPPED = 20.0
    }
}