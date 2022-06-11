package com.jc.locationproject.ui.locations

import android.app.Application
import android.location.Location
import androidx.lifecycle.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.jc.locationproject.database.AppDatabase
import com.jc.locationproject.database.LocationLog
import com.jc.locationproject.database.LocationManager
import kotlinx.coroutines.launch
import java.util.*

class LocationsViewModel(application: Application) : AndroidViewModel(application) {

    private var firebaseDb: DatabaseReference = Firebase.database.reference

    private val locationManager by lazy { LocationManager(application) }

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

        val userId = 7
        val timestamp = Calendar.getInstance().time.time
        val log = LocationLog(timestamp, userId, location.latitude, location.longitude, timestamp)

        viewModelScope.launch {
            locationManager.insert(userId, location)
            _locations.postValue(locationManager.getAll())
        }

        firebaseDb.child("users").child(userId.toString()).child("locations").child(timestamp.toString()).setValue(log)
    }
}