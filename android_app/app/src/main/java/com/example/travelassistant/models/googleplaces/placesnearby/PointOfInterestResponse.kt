package com.example.travelassistant.models.googleplaces.placesnearby

data class PointOfInterestResponse(
    val html_attributions: List<Any>,
    val next_page_token: String,
    val results: List<PointOfInterestResult>,
    val status: String
)