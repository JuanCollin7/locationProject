package com.jc.locationproject.ui.locations

import android.app.Application
import android.location.Location
import android.util.Log
import androidx.lifecycle.*
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.jc.locationproject.database.LocationLog
import com.jc.locationproject.services.Workers.CleanerWorker
import com.jc.locationproject.services.Workers.LogSyncWorker
import com.jc.locationproject.services.LocationManager
import com.jc.locationproject.services.SharedPrefsManager.SharedPrefsKey
import com.jc.locationproject.services.SharedPrefsManager.SharedPrefsManager
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class LocationsViewModel(application: Application) : AndroidViewModel(application) {

    private val locationManager = LocationManager(application)
    private val workManager = WorkManager.getInstance(application)
    private var sharedPrefsManager = SharedPrefsManager(application)

    private val userId by lazy { sharedPrefsManager.getIntValue(SharedPrefsKey.USER_ID) }

    private val _locations = MutableLiveData<List<LocationLog>>()
    val locations: LiveData<List<LocationLog>> = _locations
    
    init {
        getLocations()
        startSyncWorker()
        startCleanerWorker()
    }

    private fun getLocations() {
        viewModelScope.launch {
            _locations.postValue(locationManager.loadAllById(userId))
        }
    }

    private fun startSyncWorker() {
        val logBuilder = PeriodicWorkRequestBuilder<LogSyncWorker>(8, TimeUnit.HOURS).build()
        workManager.enqueueUniquePeriodicWork("syncWorker", ExistingPeriodicWorkPolicy.KEEP, logBuilder)
    }

    private fun startCleanerWorker() {
        val logBuilder = PeriodicWorkRequestBuilder<CleanerWorker>(7, TimeUnit.DAYS).build()
        workManager.enqueueUniquePeriodicWork("cleanerWorker", ExistingPeriodicWorkPolicy.KEEP, logBuilder)
    }

    fun newLocation(location: Location) {

        viewModelScope.launch {
            locationManager.insert(userId, location)
            _locations.postValue(locationManager.loadAllById(userId))
        }
    }
}