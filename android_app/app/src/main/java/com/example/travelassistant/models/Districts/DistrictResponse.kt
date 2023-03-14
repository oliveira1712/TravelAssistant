package com.example.travelassistant.models.Districts

data class DistrictResponse(
    val mensagem: String,
    val resultado: List<DistrictResult>,
    val status: Boolean
)