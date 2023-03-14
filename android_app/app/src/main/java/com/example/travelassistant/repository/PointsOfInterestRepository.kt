package com.example.travelassistant.repository

import androidx.lifecycle.LiveData
import com.example.travelassistant.database.dao.SavedInterestsDao
import com.example.travelassistant.models.googleplaces.placedetails.PointOfInterestDetailsResponse
import com.example.travelassistant.models.googleplaces.placesnearby.PointOfInterestResponse
import com.example.travelassistant.models.googleplaces.placesnearby.PointOfInterestResult
import com.example.travelassistant.models.googleplaces.placesnearby.PointOfInterestResultSimplifiedRoom
import com.example.travelassistant.network.api.GooglePlacesAPI
import retrofit2.Response

class PointsOfInterestRepository(
    private val restAPI: GooglePlacesAPI, val savedInterestsDao: SavedInterestsDao
) {
    suspend fun getPlacesFromAPI(
        location: String,
        radius: String = "25000",
        type: String,
        openNow: Boolean = false,
        rankBy: String = "prominence",
        key: String,
    ): Response<PointOfInterestResponse> {
        println(rankBy)
        if (rankBy == "prominence") {
            return restAPI.getPointsOfInterestNearby(
                location = location,
                radius = radius,
                type = type,
                openNow = openNow,
                key = key,
            )
        }

        //If the rankBy param is distance then the param radius cannot be sent
        return restAPI.getPointsOfInterestNearby(
            location = location, type = type, openNow = openNow, rankBy = rankBy, key = key
        )
    }

    suspend fun getPlaceDetailsFromAPI(
        place_id: String,
        key: String,
    ): Response<PointOfInterestDetailsResponse> {
        return restAPI.getPointOfInterestDetails(place_id = place_id, key = key)
    }

    fun getSavedInterestsByUser(userEmail: String): LiveData<List<PointOfInterestResultSimplifiedRoom>> {
        return savedInterestsDao.getSavedInterests(userEmail)
    }

    suspend fun insertInterest(interest: PointOfInterestResultSimplifiedRoom) {
        val interestToSave = PointOfInterestResultSimplifiedRoom(
            place_id = interest.place_id,
            business_status = interest.business_status,
            lat = interest.lat,
            lng =interest.lng,
            name = interest.name,
            opened_now = interest.opened_now,
            photoURL = interest.photoURL,
            rating = interest.rating,
            user_ratings_total = interest.user_ratings_total,
            user_email = interest.user_email
        )
        savedInterestsDao.insertInterest(interestToSave)
    }

    suspend fun removeInterest(interest: PointOfInterestResultSimplifiedRoom) {
        savedInterestsDao.removeInterest(interest)
    }
}