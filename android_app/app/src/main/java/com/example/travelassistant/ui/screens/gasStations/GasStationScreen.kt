package com.example.travelassistant.ui.screens.gasStations

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travelassistant.R
import com.example.travelassistant.models.gasStations.GasStationResult
import com.example.travelassistant.models.gasStations.GasStationsFilters
import com.example.travelassistant.models.navigation.AppBarState
import com.example.travelassistant.ui.screens.gasStations.components.GasStationFiltersBigScreens
import com.example.travelassistant.ui.screens.gasStations.components.GasStationFiltersCustomDialog
import com.example.travelassistant.ui.screens.gasStations.components.GasStationItem
import com.example.travelassistant.ui.screens.gasStations.components.GasStationItemCustomDialog
import com.example.travelassistant.ui.shared.components.NoResults
import com.example.travelassistant.ui.theme.TravelAssistantTheme


@Composable
fun GasStationsScreen(onNavigateToMap: () -> Unit, windowSize: WindowWidthSizeClass, onComposing: (AppBarState) -> Unit) {
    val gasStationsViewModel: GasStationsViewModel = viewModel()
    var showCustomDialogWithResult by remember { mutableStateOf(false) }
    var selectedFilters by remember {
        mutableStateOf(GasStationsFilters())
    }

    LaunchedEffect(key1 = true) {
        if (windowSize == WindowWidthSizeClass.Compact) {
            onComposing(AppBarState(title = "Gas Stations", actions = {
                IconButton(onClick = { showCustomDialogWithResult = true }) {
                    Icon(
                        imageVector = Icons.Filled.FilterList, contentDescription = null
                    )
                }
            }))
        } else {
            onComposing(AppBarState(title = "Gas Stations"))
        }

    }

    if (windowSize == WindowWidthSizeClass.Compact) {
        Column(
            modifier = Modifier.padding(vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (showCustomDialogWithResult) {
                GasStationFiltersCustomDialog(onDismiss = {
                    showCustomDialogWithResult = !showCustomDialogWithResult
                }, onNegativeClick = {
                    showCustomDialogWithResult = !showCustomDialogWithResult
                }, onPositiveClick = { filters ->
                    selectedFilters = filters
                    showCustomDialogWithResult = !showCustomDialogWithResult
                    gasStationsViewModel.getGasStationsFromAPI(
                        selectedFilters.fuelType,
                        selectedFilters.district,
                        selectedFilters.county
                    )
                }, gasStationsViewModel = gasStationsViewModel, filters = selectedFilters
                )
            }
        }
    }

    val districtId = gasStationsViewModel.getDistrictByDescritivoFromDB(selectedFilters.district).observeAsState()
    val municipioId = gasStationsViewModel.getMunicipioByDescritivoFromDB(selectedFilters.county).observeAsState()
    val tipoCombId = gasStationsViewModel.getFuelTypeByDescritivoFromDB(selectedFilters.fuelType).observeAsState()

    if(districtId.value?.Id != null && municipioId.value?.Id != null && tipoCombId.value?.Id != null){
        val gasStationsFromDB = gasStationsViewModel.getGasStationsByDistrictMunicipioAndFuelTypeFromDB(
            selectedFilters.district, selectedFilters.county, selectedFilters.fuelType).observeAsState()

        gasStationsViewModel.checkDataIntegrity(districtId.value?.Id!!, selectedFilters.district, municipioId.value?.Id!!.toString(), selectedFilters.county, tipoCombId.value?.Id!!.toString(), selectedFilters.fuelType, gasStationsFromDB)
    }

    val gasStations = gasStationsViewModel.getGasStationsByDistrictMunicipioAndFuelTypeFromDB(
        selectedFilters.district, selectedFilters.county, selectedFilters.fuelType
    ).observeAsState()

    Row(modifier = Modifier.fillMaxSize()) {
        if (windowSize != WindowWidthSizeClass.Compact) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.32f)
                    .fillMaxHeight(),
                //verticalArrangement = Arrangement.Center
            ) {
                GasStationFiltersBigScreens(
                    onPositiveClick = { filters ->
                        selectedFilters = filters
                        showCustomDialogWithResult = !showCustomDialogWithResult
                        gasStationsViewModel.getGasStationsFromAPI(
                            selectedFilters.fuelType,
                            selectedFilters.district,
                            selectedFilters.county
                        )
                    }, gasStationsViewModel = gasStationsViewModel, filters = selectedFilters
                )
            }
        }
        gasStations.value?.let { stations ->
            if (stations.isNotEmpty()) {
                LazyColumn {
                    items(stations.size) { item ->
                        GasStationItem(onNavigateToMap, stations[item], onClick = {})
                    }
                }
            } else {
                NoResults(
                    stringResource(id = R.string.no_results),
                    stringResource(id = R.string.no_more_results)
                )
            }
        }
    }
}

@Preview
@Composable
fun GasStationsScreenPreview() {
    var appBarState by remember {
        mutableStateOf(AppBarState())
    }

    TravelAssistantTheme {
        GasStationsScreen({}, windowSize = WindowWidthSizeClass.Compact){
            appBarState = it
        }
    }
}
