package com.example.travelassistant.network.api

import com.example.travelassistant.models.googleplaces.placedetails.PointOfInterestDetailsResponse
import com.example.travelassistant.models.googleplaces.placesnearby.PointOfInterestResponse
import retrofit2.Response

import retrofit2.http.GET
import retrofit2.http.Query

interface GooglePlacesAPI {
    @GET("nearbysearch/json")
    suspend fun getPointsOfInterestNearby(
        @Query("location") location: String,
        @Query("radius") radius: String,
        @Query("type") type: String,
        @Query("opennow") openNow: Boolean,
        @Query("key") key: String,
        ): Response<PointOfInterestResponse>

    //Google places API does not let the param radius be associated with the param rankby
    @GET("nearbysearch/json")
    suspend fun getPointsOfInterestNearby(
        @Query("location") location: String,
        @Query("type") type: String,
        @Query("opennow") openNow: Boolean,
        @Query("rankby") rankBy: String,
        @Query("key") key: String,
    ): Response<PointOfInterestResponse>

    @GET("details/json")
    suspend fun getPointOfInterestDetails(
        @Query("place_id") place_id: String,
        @Query("key") key: String,
    ): Response<PointOfInterestDetailsResponse>
}