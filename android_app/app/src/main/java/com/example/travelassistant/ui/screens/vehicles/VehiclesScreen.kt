package com.example.travelassistant.ui.screens.vehicles

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travelassistant.R
import com.example.travelassistant.models.navigation.AppBarState
import com.example.travelassistant.ui.screens.routes.components.RoutesCard
import com.example.travelassistant.ui.screens.vehicles.components.VehicleCard
import com.example.travelassistant.ui.screens.vehicles.components.VehicleDialogInfoLine
import com.example.travelassistant.ui.theme.TravelAssistantTheme
import com.example.travelassistant.viewmodels.TravelAssistantViewModel

@Composable
fun VehiclesScreen(onNavigateToInsertVehicle: () -> Unit, onNavigateBack: () -> Unit, onComposing: (AppBarState) -> Unit) {
    val screenName = stringResource(id = R.string.vehicles_screen)

    LaunchedEffect(key1 = true) {
        onComposing(
            AppBarState(
                title = screenName,
                navigationIcon = {
                    IconButton(onClick = { onNavigateBack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
            )
        )
    }

    val vehiclesViewModels: VehiclesViewModel = viewModel()
    val travelAssistantViewModel: TravelAssistantViewModel = viewModel()

    val vehicles = vehiclesViewModels.allVehicles.observeAsState()
    val savedVehicle = travelAssistantViewModel.getSelectedVehicle().collectAsState("")

    
    Box(modifier = Modifier.fillMaxSize()
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(start = 10.dp, end = 10.dp, top = 15.dp),
            modifier = Modifier
                .fillMaxHeight()
                .padding(top = 10.dp),
            verticalArrangement = Arrangement.Top
        ) {
            vehicles.value?.let {
                items(it) { item ->
                    VehicleCard(vehicle = item, savedVehicle.value)
                }
            }
        }

        FloatingActionButton(
            onClick = { onNavigateToInsertVehicle() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(10.dp),
            backgroundColor = MaterialTheme.colors.primary)
        {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Add Vehicle",
                tint = Color.White
            )
        }
    }
}

@Preview
@Composable
fun VehiclesScreenPreview() {
    TravelAssistantTheme {
        VehiclesScreen(onNavigateBack = {}, onNavigateToInsertVehicle = {}, onComposing = {})
    }
}