package com.jc.locationproject.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(val id: Int = 0, val name: String = "", val lastLat: Double = 0.0, val lastLon: Double = 0.0, val displacement: Double = 0.0, val lastTimestamp: Long = 0): Parcelable