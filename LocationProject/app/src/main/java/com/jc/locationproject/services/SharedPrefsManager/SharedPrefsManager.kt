package com.jc.locationproject.services.SharedPrefsManager

import android.content.Context
import com.jc.locationproject.BuildConfig

class SharedPrefsManager(context: Context) {

    private val sharedPref = context?.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)

    fun updateIntValue(key: SharedPrefsKey, value: Int) {
        with(sharedPref.edit()) {
            putInt(key.name, value)
        }
    }

    fun getIntValue(key: SharedPrefsKey): Int {
        return sharedPref.getInt(key.name, -1)
    }

    companion object {
        internal const val SHARED_PREFS_NAME =
            "${BuildConfig.APPLICATION_ID}.SHARED_PREFS"
    }
}