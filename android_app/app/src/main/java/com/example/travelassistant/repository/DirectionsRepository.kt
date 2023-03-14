package com.example.travelassistant.repository

import com.example.travelassistant.models.googledirections.DirectionRequestBody
import com.example.travelassistant.models.googledirections.DirectionResponse
import com.example.travelassistant.network.api.GoogleRoutesAPI
import retrofit2.Response

class DirectionsRepository(private val restAPI: GoogleRoutesAPI){
    suspend fun getDirectionsBetweenTwoPoints(
        requestBody: DirectionRequestBody
    ): Response<DirectionResponse> {
        return restAPI.getDirectionsBetweenTwoPoints(requestBody)
    }
}