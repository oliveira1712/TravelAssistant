package com.example.travelassistant.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "userVehicles", primaryKeys = ["licensePlate", "userEmail"])
data class Vehicle(
    @ColumnInfo(name = "licensePlate")
    val licencePlate: String,

    @ColumnInfo(name = "userEmail")
    val userEmail :  String,

    @ColumnInfo(name = "brand")
    val brand: String,

    @ColumnInfo(name = "model")
    val model: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name="kms")
    val kms: Double,

    @ColumnInfo(name="imageUrl")
    val imageUrl: String
)