package com.example.travelassistant.repository

import com.example.travelassistant.models.googlegeocoding.LocationGeocode
import com.example.travelassistant.network.api.GoogleGeocodeAPI
import retrofit2.Response

class GeocodeRepository(private val restAPI: GoogleGeocodeAPI) {
    suspend fun getLocationDetailsFromCoordinates(
        latlng: String,
        key: String,
    ): Response<LocationGeocode> {
        return restAPI.getLocationDetailsFromCoordinates(latlng, key)
    }
}