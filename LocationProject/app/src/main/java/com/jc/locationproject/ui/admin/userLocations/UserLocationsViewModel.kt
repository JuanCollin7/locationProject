package com.jc.locationproject.ui.admin.userLocations

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jc.locationproject.database.LocationLog
import com.jc.locationproject.models.User

class UserLocationsViewModel(application: Application) : AndroidViewModel(application) {

    private var _user: User? = null

    private val _locations = MutableLiveData<List<LocationLog>>()
    val locations: LiveData<List<LocationLog>> = _locations

    init {}

    fun set(user: User) {
        _user = user
        getLocations()
    }

    private fun getLocations() {
        _locations.value = listOf(LocationLog(0, 0, -45.0, 45.0, 102003, false))
        // TODO: Call firebase to get user's locations
    }
}