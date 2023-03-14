package com.example.travelassistant.models.googleplaces.placedetails

data class OpeningHours(
    val open_now: Boolean,
    val periods: List<Period>,
    val weekday_text: List<String>
)