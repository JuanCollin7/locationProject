package com.jc.locationproject.ui.admin.userLocations

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jc.locationproject.database.LocationLog
import com.jc.locationproject.models.User
import com.jc.locationproject.services.FirebaseManager

class UserLocationsViewModel(application: Application) : AndroidViewModel(application) {

    private val firebaseManager = FirebaseManager(application)

    private var _user: User? = null

    private val _locations = MutableLiveData<List<LocationLog>>()
    val locations: LiveData<List<LocationLog>> = _locations

    init {}

    fun set(user: User) {
        _user = user
        getLocations()
    }

    private fun getLocations() {
        _user?.let {
            firebaseManager.getUserLocations(it.id) { locations, _ ->
                _locations.value = locations
            }
        }
    }
}