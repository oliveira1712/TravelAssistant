package com.example.travelassistant.models.gasStations

data class GasStationResponse(
    val mensagem: String,
    val resultado: List<GasStationResult>,
    val status: Boolean
)