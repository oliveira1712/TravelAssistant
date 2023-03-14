package com.example.travelassistant.repository

import com.example.travelassistant.models.VisitedLocation
import com.example.travelassistant.models.googlegeocoding.Geocode
import com.example.travelassistant.models.location.LocationDetails
import com.example.travelassistant.network.api.GoogleGeocodeAPI
import com.example.travelassistant.network.api.RetrofitHelper
import com.example.travelassistant.utils.Constants
import com.example.travelassistant.utils.makeCall
import kotlinx.coroutines.MainScope
import java.text.SimpleDateFormat
import java.util.*

//This repository was created in order for the service to access it, it is not a viewmodel because
//the viewmodel call requires a composable and a service is not a composable
class SavedLocationsRepository {
    private val scope = MainScope()
    private val restAPI: GoogleGeocodeAPI =
        RetrofitHelper.getInstance(Constants.BASE_URL_GOOGLE_GEOCODE_API)
            .create(GoogleGeocodeAPI::class.java)
    private val geocodeRepository: GeocodeRepository = GeocodeRepository(
        restAPI = restAPI,
    )
    //private val dataStore = UserPreferences(context)
    //private val savedVehicle = dataStore.getSelectedVehicle

    companion object {
        private val visitedLocations: ArrayList<VisitedLocation> = ArrayList()
    }


    fun addVisitedLocation(location: LocationDetails) {
        makeCall(scope, setValue = { value ->
            val addressDetailsGeocode = value.data?.results
            val visitedLocation = getAddressDetailsFromGeocodeData(addressDetailsGeocode)
            if (visitedLocation != null && checkIfCanAdd(visitedLocation)) {
                visitedLocations.add(visitedLocation)
                if(visitedLocations.size > 1){
                    visitedLocations[visitedLocations.size - 2].endTime =
                        SimpleDateFormat("yyyy-MM-dd HH:mm:ss z",
                            Locale.getDefault()).format(Calendar.getInstance().time)
                }
            }
        }, request = {
            geocodeRepository.getLocationDetailsFromCoordinates(
                latlng = "${location.latitude}, ${location.longitude}",
                key = Constants.GOOGLE_API_KEY,
            )
        })
    }

    private fun checkIfCanAdd(visitedLocation: VisitedLocation): Boolean{
        for (visitedLoc in visitedLocations.reversed()){
            if(visitedLoc.locality == visitedLocation.locality
                && visitedLoc.district == visitedLocation.district
                && visitedLoc.endTime != null){
                return true
            }
            if(visitedLoc.locality == visitedLocation.locality
                && visitedLoc.district == visitedLocation.district){
                return false
            }
        }
        return !visitedLocations.contains(visitedLocation)
    }

    private fun getAddressDetailsFromGeocodeData(addressDetails: List<Geocode>?): VisitedLocation? {
        if (addressDetails.isNullOrEmpty()) {
            return null
        }

        var locality = ""
        var district = ""

        addressDetails.forEach { addressDetail ->
            addressDetail.address_components.forEach {
                if (it.types.contains("locality")) {
                    locality = it.short_name
                }
                if (it.types.contains("administrative_area_level_1")) {
                    district = it.short_name
                }
                if (locality.isNotEmpty() && district.isNotEmpty()) {
                    return VisitedLocation(
                        locality = locality,
                        district = district,
                        startTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.getDefault()).format(Calendar.getInstance().time),
                        endTime = null
                    )
                }
            }
        }

        return null
    }

    fun getVisitedLocations(): List<VisitedLocation> {
        return visitedLocations
    }
}
