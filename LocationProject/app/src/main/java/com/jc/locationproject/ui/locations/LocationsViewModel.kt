package com.jc.locationproject.ui.locations

import android.app.Application
import android.location.Location
import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.jc.locationproject.database.LocationLog
import com.jc.locationproject.services.FirebaseManager
import com.jc.locationproject.services.LocationManager
import com.jc.locationproject.services.SharedPrefsManager.SharedPrefsKey
import com.jc.locationproject.services.SharedPrefsManager.SharedPrefsManager
import kotlinx.coroutines.launch
import java.util.*

class LocationsViewModel(application: Application) : AndroidViewModel(application) {

    private val firebaseManager = FirebaseManager(application)
    private val locationManager = LocationManager(application)
    private var sharedPrefsManager = SharedPrefsManager(application)

    private val userId by lazy { sharedPrefsManager.getIntValue(SharedPrefsKey.USER_ID) }

    private val _locations = MutableLiveData<List<LocationLog>>()
    val locations: LiveData<List<LocationLog>> = _locations


    init {
        viewModelScope.launch {
            locationManager.deleteAll()
        }
        getLocations()
    }

    private fun getLocations() {
        viewModelScope.launch {
            _locations.postValue(locationManager.getAll())
        }
    }

    fun newLocation(location: Location) {

        viewModelScope.launch {
            locationManager.insert(userId, location)
            _locations.postValue(locationManager.getAll())

            //TODO: Remove it after implementing job scheduler
            locationManager.loadLatestById(userId)?.let {
                firebaseManager.uploadLocations(listOf(it))
            }
        }
    }
}