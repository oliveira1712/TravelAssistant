package com.example.travelassistant.ui.screens.map

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.travelassistant.R
import com.example.travelassistant.database.TravelAssistantDatabase
import com.example.travelassistant.models.googledirections.*
import com.example.travelassistant.models.googleplaces.placedetails.PointOfInterestDetailsResponse
import com.example.travelassistant.models.googleplaces.placesnearby.PointOfInterestResponse
import com.example.travelassistant.network.api.GooglePlacesAPI
import com.example.travelassistant.network.api.GoogleRoutesAPI
import com.example.travelassistant.network.api.RetrofitHelper
import com.example.travelassistant.repository.DirectionsRepository
import com.example.travelassistant.repository.PointsOfInterestRepository
import com.example.travelassistant.sensors.LocationSensor
import com.example.travelassistant.utils.Constants
import com.example.travelassistant.utils.ResourceNetwork
import com.example.travelassistant.utils.makeCall
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil

class MapViewModel(application: Application) : AndroidViewModel(application) {
    private val locationLiveData = LocationSensor(application, 2)
    fun getLocationLiveData() = locationLiveData
    fun startLocationUpdates() {
        locationLiveData.startLocationUpdates()
    }

    private val restAPIDirections: GoogleRoutesAPI =
        RetrofitHelper.getInstance(Constants.BASE_URL_GOOGLE_DIRECTIONS_API)
            .create(GoogleRoutesAPI::class.java)

    private val _directionAPI = MutableLiveData<ResourceNetwork<DirectionResponse>>()
    val directionAPI: LiveData<ResourceNetwork<DirectionResponse>> = _directionAPI

    private val directionsRepository: DirectionsRepository = DirectionsRepository(
        restAPI = restAPIDirections
    )

    private val restAPIPointOfInterest: GooglePlacesAPI =
            RetrofitHelper.getInstance(Constants.BASE_URL_GOOGLE_PLACES_API)
                .create(GooglePlacesAPI::class.java)

    private val _pointsOfInterestAPI = MutableLiveData<ResourceNetwork<PointOfInterestDetailsResponse>>()
    val pointsOfInterestAPI: LiveData<ResourceNetwork<PointOfInterestDetailsResponse>> =
        _pointsOfInterestAPI

    private val db = TravelAssistantDatabase.getDatabase(application)
    private val pointsOfInterestRepository: PointsOfInterestRepository = PointsOfInterestRepository(
        restAPI = restAPIPointOfInterest,
        savedInterestsDao = db.getSavedInterestsDao(),
    )

    fun getDirectionsBetweenTwoPoints(
        requestBody: DirectionRequestBody
    ) {
        requestBody.origin =  Origin(Location(LatLngDirections(locationLiveData.value?.latitude?.toDouble() ?: 0.0, locationLiveData.value?.longitude?.toDouble() ?: 0.0)))
        Log.d("DirectionOrigin", requestBody.origin.toString())
        Log.d("DirectionDestination", requestBody.destination.toString())

        makeCall(viewModelScope = viewModelScope, setValue = { value ->
            _directionAPI.postValue(value)
        }, request = {
            directionsRepository.getDirectionsBetweenTwoPoints(requestBody)
        })
    }

    fun stopDirectionNavigation(){
        _directionAPI.postValue(null)
    }

    fun getStepFromCurrentLocation(): Step? {
        val legs = _directionAPI.value?.data?.routes?.get(0)?.legs
        var step: Step? = null
        val startingTripText = getApplication<Application>().resources.getString(R.string.starting_trip)
        val moveForwardText = getApplication<Application>().resources.getString(R.string.move_forward)

        if (legs != null) {
            for(leg in legs){
                val steps = leg.steps
                for (stepNum in steps.indices){
                    val currentLocation = LatLng(locationLiveData.value?.latitude?.toDouble() ?: 0.0,
                        locationLiveData.value?.longitude?.toDouble() ?: 0.0)
                    val decodedPoints = PolyUtil.decode(steps[stepNum].polyline.encodedPolyline)
                    if(PolyUtil.isLocationOnPath(currentLocation, decodedPoints, true, 100.0)){
                        if(steps[stepNum].navigationInstruction == null) {
                            if(stepNum == 0){
                                steps[stepNum].navigationInstruction = NavigationInstruction(startingTripText, "STARTING")
                            }else{
                                steps[stepNum].navigationInstruction = NavigationInstruction(moveForwardText, "FORWARD")
                            }
                        }
                        step = steps[stepNum]
                    }
                }
            }
        }

        return step
    }

    fun getPlaceDetailsFromAPI(
        place_id: String
    ) {
        makeCall(viewModelScope = viewModelScope, setValue = { value ->
            _pointsOfInterestAPI.postValue(value)
            Log.d("SelectedPOI", place_id)
            Log.d("SelectedPOI", value.data.toString())
        }, request = {
            pointsOfInterestRepository.getPlaceDetailsFromAPI(place_id = place_id, key = Constants.GOOGLE_API_KEY)
        })
    }
}