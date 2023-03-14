package com.example.travelassistant.utils

import android.content.Context
import com.example.travelassistant.models.googledirections.DirectionRequestBody
import com.example.travelassistant.ui.screens.map.MapViewModel

fun startNavigation(
    context: Context,
    mapViewModel: MapViewModel,
    navigationType: String,
    routeOptions: DirectionRequestBody,
    onNavigateToMap: (() -> Unit)?
) {
    if (navigationType == "TravelAssistant") {
        mapViewModel.getDirectionsBetweenTwoPoints(routeOptions)
        if (onNavigateToMap != null) onNavigateToMap()
    } else {
        val destinationLatitude = routeOptions.destination.location.latLng.latitude
        val destinationLongitude = routeOptions.destination.location.latLng.longitude
        openGoogleMapsIntent(
            context, destinationLatitude.toString(), destinationLongitude.toString()
        )
    }
}