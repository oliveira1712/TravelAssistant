package com.example.travelassistant.models

import com.example.travelassistant.models.Municipios.MunicipiosResult
import java.text.SimpleDateFormat

data class VisitedLocation (
    val locality: String,
    val district: String,
    val startTime: String,
    var endTime: String?
)
