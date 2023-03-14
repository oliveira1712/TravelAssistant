package com.example.travelassistant.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelassistant.models.VisitedLocation
import com.example.travelassistant.models.googlegeocoding.LocationGeocode
import com.example.travelassistant.models.location.LocationDetails
import com.example.travelassistant.network.api.GoogleGeocodeAPI
import com.example.travelassistant.network.api.RetrofitHelper
import com.example.travelassistant.preferences.UserPreferences
import com.example.travelassistant.repository.GeocodeRepository
import com.example.travelassistant.utils.Constants
import com.example.travelassistant.utils.NetworkConnectionHelper
import com.example.travelassistant.utils.ResourceNetwork
import com.example.travelassistant.utils.makeCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TravelAssistantViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application.applicationContext

    private val dataStore = UserPreferences(context)
    private val savedTheme = dataStore.getThemeMode
    private val isUserLoggedIn = dataStore.getIsUserLogged
    private val savedVehicle = dataStore.getSelectedVehicle
    private val savedNavigationType = dataStore.getSelectedNavigationType


    private val networkAvailability: NetworkConnectionHelper = NetworkConnectionHelper(context)

    fun getNetworkAvailability(): NetworkConnectionHelper {
        return networkAvailability
    }

    fun logInUser() {
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.setIsUserLogged(true)
        }
    }

    fun logoutUser() {
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.setIsUserLogged(false)
        }
    }

    fun getIsUserLoggedIn(): Flow<Boolean> {
        return isUserLoggedIn
    }

    fun getSavedTheme(): Flow<String> {
        return savedTheme
    }

    fun getSelectedVehicle(): Flow<String> {
        return savedVehicle
    }

    fun getNavigationType(): Flow<String> {
        return savedNavigationType
    }

    fun setTheme(theme: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.setThemeMode(theme)
        }
    }

    fun setSelectedVehicle(vehicleLicensePlate: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.setSelectedVehicle(vehicleLicensePlate)
        }
    }

    fun setNavigationType(navigationType: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.setSelectedNavigationType(navigationType)
        }
    }
}
