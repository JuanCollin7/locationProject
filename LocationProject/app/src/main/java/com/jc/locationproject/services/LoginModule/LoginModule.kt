package com.jc.locationproject.services.LoginModule

import android.content.Context
import com.jc.locationproject.models.LoginInfo
import com.jc.locationproject.services.SharedPrefsManager.SharedPrefsKey
import com.jc.locationproject.services.SharedPrefsManager.SharedPrefsManager

class LoginModule(context: Context) {

    private val sharedPrefs = SharedPrefsManager(context)

    private val accounts = mapOf("admin" to LoginInfo(0, "admin", "admin", "Admin", true),
                                 "juan" to LoginInfo(1, "juan", "12345678", "Juan Collin", false),
                                 "joao" to LoginInfo(2, "joao", "12345678", "João Beck", false),
                                 "fabiano" to LoginInfo(3, "fabiano", "12345678", "Fabiano Pacheco", false)
    )

    fun login(username: String, password: String): LoginResponse {
        val user = accounts[username] ?: return LoginResponse(false, "User not found", false)

        return if (user.password == password) {
            saveUserId(user.id)
            LoginResponse(true, "", user.isAdmin)
        }  else {
            LoginResponse(false, "Wrong Password", false)
        }
    }

    private fun saveUserId(id: Int) {
        sharedPrefs.updateIntValue(SharedPrefsKey.USER_ID, id)
    }
}