package com.example.travelassistant.models.gasStations

data class GasStationsFilters(
    var district: String = "Select a District",
    var county: String = "Select a county",
    var fuelType: String = "Select a fuel type"
)