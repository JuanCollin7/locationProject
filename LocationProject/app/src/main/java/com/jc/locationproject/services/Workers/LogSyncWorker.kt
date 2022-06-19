package com.jc.locationproject.services.Workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.jc.locationproject.services.FirebaseManager
import com.jc.locationproject.services.LocationManager
import com.jc.locationproject.services.SharedPrefsManager.SharedPrefsKey
import com.jc.locationproject.services.SharedPrefsManager.SharedPrefsManager

class LogSyncWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    private val firebaseManager = FirebaseManager(context)
    private val locationManager = LocationManager(context)
    private var sharedPrefsManager = SharedPrefsManager(context)

    private val userId by lazy { sharedPrefsManager.getIntValue(SharedPrefsKey.USER_ID) }

    override suspend fun doWork(): Result {
        val logsToSync = locationManager.getAllUnsynced(userId)
        if (logsToSync.isEmpty()) { return Result.success() }

        firebaseManager.uploadLocations(logsToSync)

        return Result.success()
    }
}