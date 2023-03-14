package com.example.travelassistant.network.api

import com.example.travelassistant.models.googlegeocoding.LocationGeocode
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleGeocodeAPI {
    @GET("json")
    suspend fun getLocationDetailsFromCoordinates(
        @Query("latlng") location: String,
        @Query("key") key: String,
    ): Response<LocationGeocode>
}