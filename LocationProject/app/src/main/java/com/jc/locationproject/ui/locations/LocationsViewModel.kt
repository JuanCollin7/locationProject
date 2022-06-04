package com.jc.locationproject.ui.locations

import android.app.Application
import android.location.Location
import android.util.Log
import androidx.lifecycle.*
import com.jc.locationproject.database.AppDatabase
import com.jc.locationproject.database.LocationLog
import kotlinx.coroutines.launch
import java.util.*

class LocationsViewModel(application: Application) : AndroidViewModel(application) {

    private val database by lazy { AppDatabase.getDatabase(application) }

    private val _locations = MutableLiveData<List<LocationLog>>()
    val locations: LiveData<List<LocationLog>> = _locations

    init {
        viewModelScope.launch {
            database.locationLogDao().deleteAll()
        }
        getLocations()
    }

    private fun getLocations() {
        viewModelScope.launch {
            _locations.postValue(database.locationLogDao().getAll())
        }
    }

    fun newLocation(location: Location) {
        viewModelScope.launch {
            val timestamp = Calendar.getInstance().time.time

            database.locationLogDao().insertAll(LocationLog(0L, 0, location.latitude, location.longitude, timestamp))
            _locations.postValue(database.locationLogDao().getAll())
        }
    }
}