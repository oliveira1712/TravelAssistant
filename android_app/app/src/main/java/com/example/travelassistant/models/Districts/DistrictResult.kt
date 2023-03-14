package com.example.travelassistant.models.Districts

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "districts")
data class DistrictResult (
    @PrimaryKey
    val Descritivo: String,

    @ColumnInfo(name = "Id")
    val Id: String,
)