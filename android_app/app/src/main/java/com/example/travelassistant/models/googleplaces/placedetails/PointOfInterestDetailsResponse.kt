package com.example.travelassistant.models.googleplaces.placedetails

data class PointOfInterestDetailsResponse(
    val html_attributions: List<Any>,
    val result: PointOfInterestDetailsResult,
    val status: String
)