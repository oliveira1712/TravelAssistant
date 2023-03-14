package com.example.travelassistant.ui.screens.tripDetails

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.travelassistant.database.TravelAssistantDatabase
import com.example.travelassistant.models.Route
import com.example.travelassistant.repository.RoutesRepository
import com.example.travelassistant.repository.SavedLocationsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TripDetailsViewModel (application: Application): AndroidViewModel(application) {
    private val repository: SavedLocationsRepository = SavedLocationsRepository()
    val visitedLocations = repository.getVisitedLocations()
}