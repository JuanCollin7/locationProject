package com.jc.locationproject.services.SharedPrefsManager

import android.content.Context
import android.util.Log
import com.jc.locationproject.BuildConfig

class SharedPrefsManager(context: Context) {

    private val sharedPref = context?.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)

    fun updateIntValue(key: SharedPrefsKey, value: Int) {
        val edit = sharedPref.edit()
        edit.putInt(key.name, value)
        edit.apply()
    }

    fun getIntValue(key: SharedPrefsKey): Int {
        val value = sharedPref.getInt(key.name, -1)
        return value
    }

    companion object {
        internal const val SHARED_PREFS_NAME =
            "${BuildConfig.APPLICATION_ID}.SHARED_PREFS"
    }
}