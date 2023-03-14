package com.example.travelassistant.network.api

import com.example.travelassistant.models.googledirections.DirectionResponse
import com.example.travelassistant.models.googledirections.DirectionRequestBody
import com.example.travelassistant.utils.Constants.GOOGLE_API_KEY
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface GoogleRoutesAPI {
    @Headers(
        "Content-Type: application/json",
        "X-Goog-FieldMask: *",
        "X-Goog-Api-Key: $GOOGLE_API_KEY"
    )
    @POST(".")
    suspend fun getDirectionsBetweenTwoPoints(
        @Body requestBody: DirectionRequestBody
    ): Response<DirectionResponse>
}