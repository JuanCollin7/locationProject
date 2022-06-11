package com.jc.locationproject.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(val id: Int, val name: String, val lastLat: Double, val lastLon: Double, val displacement: Double, val lastTimestamp: Long): Parcelable