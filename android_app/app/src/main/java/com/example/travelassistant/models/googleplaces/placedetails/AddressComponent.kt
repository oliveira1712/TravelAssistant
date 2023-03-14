package com.example.travelassistant.models.googleplaces.placedetails

data class AddressComponent(
    val long_name: String,
    val short_name: String,
    val types: List<String>
)