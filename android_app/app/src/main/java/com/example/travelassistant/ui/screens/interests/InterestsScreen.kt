package com.example.travelassistant.ui.screens.interests

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travelassistant.R
import com.example.travelassistant.models.googleplaces.placesnearby.PlaceType
import com.example.travelassistant.models.googleplaces.placesnearby.PointsOfInterestFilters
import com.example.travelassistant.models.googleplaces.placesnearby.getPlaceTypeValue
import com.example.travelassistant.models.location.LocationDetails
import com.example.travelassistant.models.navigation.AppBarState
import com.example.travelassistant.sensors.locationPermissions
import com.example.travelassistant.ui.screens.gasStations.GasStationsScreen
import com.example.travelassistant.ui.screens.interests.components.ChipGroup
import com.example.travelassistant.ui.screens.interests.components.DrawSwipableCards
import com.example.travelassistant.ui.screens.interests.components.FiltersCustomDialog
import com.example.travelassistant.ui.screens.interests.components.MySwippableCardShimmer
import com.example.travelassistant.ui.shared.components.NoResults
import com.example.travelassistant.ui.theme.TravelAssistantTheme
import com.example.travelassistant.viewmodels.TravelAssistantViewModel

@Composable
fun InterestsScreen(onNavigateToMap: () -> Unit, onComposing: (AppBarState) -> Unit) {
    val screenName = stringResource(id = R.string.interest_screen)
    var showFiltersDialog by remember { mutableStateOf(false) }
    var selectedFilters by remember {
        mutableStateOf(PointsOfInterestFilters())
    }

    val interestsViewModel: InterestsViewModel = viewModel()
    val travelAssistantViewModel: TravelAssistantViewModel = viewModel()
    val places = interestsViewModel.pointsOfInterestAPI.observeAsState()
    val currentLocation = interestsViewModel.getLocationLiveData().observeAsState()
    val navigationType = travelAssistantViewModel.getNavigationType().collectAsState(initial = "")
    val locationPermissions = locationPermissions()
    val isNetworkAvailable = travelAssistantViewModel.getNetworkAvailability().observeAsState(false)

    LaunchedEffect(key1 = currentLocation.value, key2 = isNetworkAvailable.value) {
        onComposing(AppBarState(title = screenName, actions = {
            IconButton(onClick = { showFiltersDialog = true }) {
                Icon(
                    imageVector = Icons.Default.FilterList, contentDescription = null
                )
            }
        }))
        if (locationPermissions == 2) {
            currentLocation.value?.let { location ->
                makeAPICall(
                    interestsViewModel, selectedFilters, location, isNetworkAvailable.value
                )
            }
        }
    }

    if (showFiltersDialog) {
        FiltersCustomDialog(onDismiss = {
            showFiltersDialog = !showFiltersDialog
        }, onNegativeClick = {
            showFiltersDialog = !showFiltersDialog
        }, onPositiveClick = { filters ->
            //store the last selected place type
            filters.type = selectedFilters.type
            selectedFilters = filters
            currentLocation.value?.let { location ->
                makeAPICall(
                    interestsViewModel, selectedFilters, location, isNetworkAvailable.value
                )
            }
            showFiltersDialog = !showFiltersDialog
        }, filters = selectedFilters)
    }


    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ChipGroup(placeTypes = interestsViewModel.placeTypes,
            selectedPlaceType = selectedFilters.type.value,
            onSelectedChanged = { selectedPlaceType ->
                selectedFilters.type = getPlaceTypeValue(selectedPlaceType) ?: PlaceType.Restaurants

                currentLocation.value?.let { location ->
                    makeAPICall(
                        interestsViewModel, selectedFilters, location, isNetworkAvailable.value
                    )
                }
            })

        if (places.value?.data == null) {
            //If there is any error message, display it
            if (places.value?.message?.isNotEmpty() == true) {
                Log.d("Exception", places.value?.message!!)
            } else {
                //There is no error, but the data is still loading
                //Show the loading effect then
                MySwippableCardShimmer()
            }
        } else {
            //If the data returned is empty, alert the user
            if (places.value?.data?.results?.size == 0) {
                NoResults(
                    title = stringResource(id = R.string.no_results),
                    description = stringResource(id = R.string.no_results_sub_text)
                )
            } else {
                //If there is data, show it
                DrawSwipableCards(onNavigateToMap, currentLocation.value, navigationType.value)
            }
        }
    }
}

@Preview
@Composable
fun InterestsScreenPreview() {
    var appBarState by remember {
        mutableStateOf(AppBarState())
    }

    TravelAssistantTheme {
        InterestsScreen(onNavigateToMap = {}){
            appBarState = it
        }
    }
}

private fun makeAPICall(
    interestsViewModel: InterestsViewModel,
    selectedFilters: PointsOfInterestFilters,
    location: LocationDetails,
    isNetworkAvailable: Boolean,
) {
    if (isNetworkAvailable) {
        interestsViewModel.getPlacesFromAPI(
            location = "${location.latitude}, ${location.longitude}",
            radius = (selectedFilters.radius * 1000).toString(),
            type = selectedFilters.type.value,
            openNow = selectedFilters.openNow,
            rankBy = selectedFilters.rankBy,
        )

        Log.d("NetworkTest", "Connection Available")
    } else {
        Log.d("NetworkTest", "Connection Unavailable")
    }
}
