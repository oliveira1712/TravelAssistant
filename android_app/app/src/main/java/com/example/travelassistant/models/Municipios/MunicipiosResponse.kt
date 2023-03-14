package com.example.travelassistant.models.Municipios

data class MunicipiosResponse (
    val mensagem: String,
    val resultado: List<MunicipiosResult>,
    val status: Boolean
        )