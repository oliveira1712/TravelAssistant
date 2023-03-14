package com.example.travelassistant.models.gasStations.FuelType

data class FuelTypeResponse (
        val mensagem: String,
        val resultado: List<FuelTypeResult>,
        val status: Boolean
    )