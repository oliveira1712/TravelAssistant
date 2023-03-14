package com.example.travelassistant.models

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "routes", primaryKeys = ["userEmail", "endPointLat", "endPointLon"])
data class Route (
    @ColumnInfo(name = "userEmail")
    val userEmail :  String,

    @ColumnInfo(name = "endPoint")
    val endPoint: String,

    @ColumnInfo(name = "endPointLat")
    val endPointLat: Double,

    @ColumnInfo(name = "endPointLon")
    val endPointLon: Double,
)