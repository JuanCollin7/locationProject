package com.jc.locationproject.ui.login

import android.app.Application
import androidx.lifecycle.*

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    init { }

    fun setUsername(username: String) {
        _username.value = username
    }

    fun setPassword(password: String) {
        _password.value = password
    }

    fun mustEnableLoginButton(): Boolean {
        return !username.value.isNullOrBlank() && !password.value.isNullOrBlank()
    }
}