package com.example.travelassistant.models.googlegeocoding

data class Geometry(
    val bounds: Bounds,
    val location: Location,
    val location_type: String,
    val viewport: Viewport
)