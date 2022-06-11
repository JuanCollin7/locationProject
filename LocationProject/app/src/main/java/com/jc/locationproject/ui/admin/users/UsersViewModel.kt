package com.jc.locationproject.ui.admin.users

import android.app.Application
import android.location.Location
import androidx.lifecycle.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.jc.locationproject.database.AppDatabase
import com.jc.locationproject.models.User
import com.jc.locationproject.services.FirebaseManager
import kotlinx.coroutines.launch

class UsersViewModel(application: Application) : AndroidViewModel(application) {

    private val firebaseManager = FirebaseManager(application)

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    init {
        getUsers()
    }

    private fun getUsers() {
        firebaseManager.getUsers { users, _ ->
            _users.value = users
        }
    }
}