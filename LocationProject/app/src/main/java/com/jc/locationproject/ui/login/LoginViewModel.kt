package com.jc.locationproject.ui.login

import android.app.Application
import androidx.lifecycle.*
import com.jc.locationproject.services.LoginModule.LoginModule
import com.jc.locationproject.services.LoginModule.LoginResponse

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val loginModule = LoginModule(application)

    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse

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

    fun login() {
        _loginResponse.value = loginModule.login(username.value ?: "", password.value ?: "")
    }
}