package com.example.travelassistant.models.googledirections

data class SpeedReadingInterval(
    val endPolylinePointIndex: Int,
    val speed: String,
    val startPolylinePointIndex: Int
)