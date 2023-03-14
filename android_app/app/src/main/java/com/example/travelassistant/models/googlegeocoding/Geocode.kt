package com.example.travelassistant.models.googlegeocoding

data class Geocode(
    val address_components: List<AddressComponent>,
    val formatted_address: String,
    val geometry: Geometry,
    val place_id: String,
    val plus_code: PlusCode,
    val types: List<String>
)