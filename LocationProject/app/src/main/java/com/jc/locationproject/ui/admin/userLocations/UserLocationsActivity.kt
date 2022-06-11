package com.jc.locationproject.ui.admin.userLocations

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.jc.locationproject.R
import com.jc.locationproject.models.User

class UserLocationsActivity : AppCompatActivity() {

    private val viewModel: UserLocationsViewModel by lazy {
        ViewModelProvider(this)[UserLocationsViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_locations)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, UserLocationsFragment.newInstance())
                .commitNow()
        }

        val user = intent.getParcelableExtra<User>(UserLocationsActivity.ARG_USER) ?: return
        viewModel.set(user)
    }

    companion object {
        private const val ARG_USER = "USER"

        fun getInstance(context: Context, user: User) = Intent(context, UserLocationsActivity::class.java).apply {
            putExtra(ARG_USER, user)
        }
    }
}