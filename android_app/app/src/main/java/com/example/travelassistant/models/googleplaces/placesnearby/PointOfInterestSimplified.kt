package com.example.travelassistant.models.googleplaces.placesnearby

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "savedinterests")
data class PointOfInterestResultSimplifiedRoom(
    @PrimaryKey
    val place_id: String,
    val business_status: String,
    val lat: String,
    val lng: String,
    val name: String,
    val opened_now: Boolean?,
    val photoURL: String?,
    val rating: Double,
    val user_ratings_total: Int,
    val user_email: String,
)