package com.example.travelassistant.models.googleplaces.placesnearby

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.travelassistant.R

enum class PlaceType(val value: String, var id: Int, var icon: ImageVector){
    Restaurants("restaurant", R.string.place_type_restaurants, Icons.Default.Restaurant),
    SuperMarkets("supermarket", R.string.place_type_supermarkets, Icons.Default.LocalGroceryStore),
    Gyms("gym", R.string.place_type_gyms, Icons.Default.FitnessCenter),
    ArtGalleries("art_gallery", R.string.place_type_artgalleries, Icons.Default.Palette),
    Parking("parking", R.string.place_type_parking, Icons.Default.LocalParking)
}

fun getPlaceTypeValue(value: String): PlaceType? {
    val map = PlaceType.values().associateBy(PlaceType::value)
    return map[value]
}