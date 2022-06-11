package com.jc.locationproject.ui.admin.users

import android.app.Application
import android.location.Location
import androidx.lifecycle.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.jc.locationproject.database.AppDatabase
import com.jc.locationproject.models.User
import kotlinx.coroutines.launch

class UsersViewModel(application: Application) : AndroidViewModel(application) {

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    init {
        getUsers()
    }

    private fun getUsers() {
        _users.value = listOf(User(0, "Juan Collin", -45.0, 45.0, 20.0, 10230230))
    }
}