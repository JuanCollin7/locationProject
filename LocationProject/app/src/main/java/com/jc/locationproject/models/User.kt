package com.jc.locationproject.models

data class User(val id: Int, val name: String, val lastLat: Double, val lastLon: Double, val displacement: Double, val lastTimestamp: Long)