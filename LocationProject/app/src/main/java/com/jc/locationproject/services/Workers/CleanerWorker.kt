package com.jc.locationproject.services.Workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.jc.locationproject.services.LocationManager

class CleanerWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    private val locationManager = LocationManager(context)

    override suspend fun doWork(): Result {
        locationManager.deleteAllSynced()
        return Result.success()
    }
}