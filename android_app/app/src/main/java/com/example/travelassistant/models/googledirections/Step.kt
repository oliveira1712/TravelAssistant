package com.example.travelassistant.models.googledirections

data class Step(
    val distanceMeters: Int,
    val endLocation: EndLocation,
    var navigationInstruction: NavigationInstruction?,
    val polyline: Polyline,
    val startLocation: StartLocation,
    val staticDuration: String
)