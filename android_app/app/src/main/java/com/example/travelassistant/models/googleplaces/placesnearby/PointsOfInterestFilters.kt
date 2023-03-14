package com.example.travelassistant.models.googleplaces.placesnearby

data class PointsOfInterestFilters(
    var openNow: Boolean = false,
    var rankBy: String = "prominence",
    var radius: Int = 25,
    var type: PlaceType = PlaceType.Restaurants
)
