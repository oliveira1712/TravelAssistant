package com.example.travelassistant.models.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.travelassistant.R

sealed class NavigationItem(var route: String, var stringResourceId: Int, var iconFilled: ImageVector, var iconOutlined: ImageVector) {
    object Map : NavigationItem("map", R.string.map_screen, Icons.Filled.Map, Icons.Outlined.Map)
    object Interests : NavigationItem("interests", R.string.interest_screen, Icons.Filled.LocationOn, Icons.Outlined.LocationOn)
    object More : NavigationItem("more", R.string.more_screen, Icons.Filled.AddCircle, Icons.Outlined.AddCircleOutline)
    object Stations : NavigationItem("gasStations", R.string.stations, Icons.Filled.LocalGasStation, Icons.Outlined.LocalGasStation)
}