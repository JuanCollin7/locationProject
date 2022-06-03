package com.jc.locationproject.ui.locations

import android.app.Application
import android.location.Location
import android.util.Log
import androidx.lifecycle.*
import com.jc.locationproject.database.AppDatabase
import com.jc.locationproject.database.LocationLog
import kotlinx.coroutines.launch

class LocationsViewModel(application: Application) : AndroidViewModel(application) {

    private val database by lazy { AppDatabase.getDatabase(application) }

    private val _locations = MutableLiveData<List<LocationLog>>()
    val locations: LiveData<List<LocationLog>> = _locations

    init {
        //fillDatabase()
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
            database.locationLogDao().insertAll(LocationLog(0L, 0, location.latitude, location.longitude, 0L))
            _locations.postValue(database.locationLogDao().getAll())
        }
    }

    private fun fillDatabase() {
        viewModelScope.launch {
            database.locationLogDao().insertAll(LocationLog(0L, 0, 37.445772, -122.149578, 0L),
                                                LocationLog(0L, 0, 3.3, 3.3, 0L))
        }
    }
}