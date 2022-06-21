package com.jc.locationproject.services.Workers

import android.content.Context
import android.util.Log
import androidx.work.*
import com.jc.locationproject.services.FirebaseManager
import com.jc.locationproject.services.LocationManager
import com.jc.locationproject.services.SharedPrefsManager.SharedPrefsKey
import com.jc.locationproject.services.SharedPrefsManager.SharedPrefsManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import java.util.concurrent.TimeUnit

class LogSyncWorker(val context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    private val firebaseManager = FirebaseManager(context)
    private val locationManager = LocationManager(context)
    private var sharedPrefsManager = SharedPrefsManager(context)

    private val userId by lazy { sharedPrefsManager.getIntValue(SharedPrefsKey.USER_ID) }

    override suspend fun doWork(): Result {
        Log.v("WORKER", "SYNC WORKER")
        val logsToSync = locationManager.getAllUnsynced(userId)
        if (logsToSync.isEmpty()) {
            return Result.success()
        }

        firebaseManager.uploadLocations(logsToSync)

        run(context)

        return Result.success()
    }

    companion object {
        fun run(context: Context) {

            val dueDate = Calendar.getInstance()
            val currentDate = Calendar.getInstance()
            dueDate.set(Calendar.HOUR_OF_DAY, 1)
            dueDate.set(Calendar.MINUTE, 0)
            dueDate.set(Calendar.SECOND, 0)

            if (dueDate.before(currentDate)) {
                dueDate.add(Calendar.HOUR_OF_DAY, 24)
            }
            val timeDiff = dueDate.timeInMillis - currentDate.timeInMillis
            val dailyWorkRequest = OneTimeWorkRequestBuilder<LogSyncWorker>()
                .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
                .build()

            WorkManager.getInstance(context).enqueue(dailyWorkRequest)
        }
    }
}