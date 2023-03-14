package com.example.travelassistant.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity

//modes
//d for driving
//w for walking
//b for bicycling
fun openGoogleMapsIntent(
    context: Context, destinationLatitude: String, destinationLongitude: String, mode: String = "d"
) {
    val gmmIntentUri =
        Uri.parse("google.navigation:q=$destinationLatitude,$destinationLongitude&mode=$mode")
    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
    mapIntent.setPackage("com.google.android.apps.maps")
    startActivity(context, mapIntent, null)
}