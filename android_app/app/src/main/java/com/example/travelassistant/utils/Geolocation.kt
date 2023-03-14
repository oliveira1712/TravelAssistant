package com.example.travelassistant.utils

import android.annotation.SuppressLint
import android.location.Location
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.travelassistant.models.location.LocationDetails
import com.google.android.gms.location.*


//Example of call
// Expected return -> 55.96Km
// getDistanceBetweenCoordinates(41.355756326180526, -8.194561261306937, 40.93022898102309, -8.551595737298797)
fun getDistanceBetweenCoordinates(
    startLatitude: Double,
    startLongitude: Double,
    endLatitude: Double,
    endLongitude: Double
): Double {
    val locationA = Location("point A")

    locationA.latitude = startLatitude
    locationA.longitude = startLongitude

    val locationB = Location("point B")

    locationB.latitude = endLatitude
    locationB.longitude = endLongitude

    return locationA.distanceTo(locationB).toDouble()
}