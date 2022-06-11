package com.jc.locationproject.services.LoginModule

data class LoginResponse(val didLogin: Boolean, val errorMessage: String, val isAdmin: Boolean)