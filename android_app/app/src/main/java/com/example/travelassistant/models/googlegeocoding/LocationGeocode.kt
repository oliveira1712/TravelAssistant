package com.example.travelassistant.models.googlegeocoding

data class LocationGeocode(
    val plus_code: PlusCode,
    val results: List<Geocode>,
    val status: String
)