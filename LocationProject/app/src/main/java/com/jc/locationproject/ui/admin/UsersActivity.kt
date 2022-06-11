package com.jc.locationproject.ui.admin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jc.locationproject.R
import com.jc.locationproject.ui.locations.LocationsActivity

class UsersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, UsersFragment.newInstance())
                .commitNow()
        }
    }

    companion object {
        fun getInstance(context: Context) = Intent(context, UsersActivity::class.java)
    }
}