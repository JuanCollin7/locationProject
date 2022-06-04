package com.jc.locationproject.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locationLog")
data class LocationLog(
    @PrimaryKey(autoGenerate = true)
    var uid: Long = 0,
    var userId: Int? = null,
    var lat: Double? = null,
    var lon: Double? = null,
    var timestamp: Long? = null
)