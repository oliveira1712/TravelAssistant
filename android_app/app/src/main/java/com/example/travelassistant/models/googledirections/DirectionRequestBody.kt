package com.example.travelassistant.models.googledirections

data class DirectionRequestBody(
    val destination: Destination ,
    var origin: Origin = Origin(Location(LatLngDirections(0.0,0.0))),
    val polylineEncoding: String = "ENCODED_POLYLINE",
    val polylineQuality: String = "HIGH_QUALITY",
    val routeModifiers: RouteModifiers,
    val routingPreference: String = "TRAFFIC_AWARE",
    val travelMode: String = "DRIVE"
)