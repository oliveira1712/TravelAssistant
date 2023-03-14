package com.example.travelassistant.models.gasStations.FuelType

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fuelTypes")
data class FuelTypeResult (
    @PrimaryKey
    val Id: Int,

    @ColumnInfo(name = "descritivo")
    val Descritivo: String,
)