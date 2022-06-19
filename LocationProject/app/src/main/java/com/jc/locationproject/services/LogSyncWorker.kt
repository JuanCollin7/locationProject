package com.jc.locationproject.services

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class LogSyncWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    private val firebaseManager = FirebaseManager(context)
    private val locationManager = LocationManager(context)

    override suspend fun doWork(): Result {

        val logsToSync = locationManager.getAllUnsynced()
        if (logsToSync.isEmpty()) { return Result.success() }

        firebaseManager.uploadLocations(logsToSync)

        return Result.success()
    }
}