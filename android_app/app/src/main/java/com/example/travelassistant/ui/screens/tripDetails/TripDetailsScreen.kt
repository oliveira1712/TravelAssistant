package com.example.travelassistant.ui.screens.tripDetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travelassistant.R
import com.example.travelassistant.models.navigation.AppBarState
import com.example.travelassistant.ui.screens.routes.RoutesScreen
import com.example.travelassistant.ui.screens.tripDetails.components.DateSection
import com.example.travelassistant.ui.screens.tripDetails.components.TripDetailsCard
import com.example.travelassistant.ui.theme.TravelAssistantTheme

@Composable
fun TripDetailsScreen(onNavigateBack: () -> Unit, onComposing: (AppBarState) -> Unit) {
    val screenName = stringResource(id = R.string.tripDetails_screen)
    val tripDetailsViewModel: TripDetailsViewModel = viewModel()
    val visitedLocations = tripDetailsViewModel.visitedLocations

    LaunchedEffect(key1 = true) {
        onComposing(
            AppBarState(
                title = screenName,
                navigationIcon = {
                    IconButton(onClick = { onNavigateBack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack, contentDescription = null
                        )
                    }
                },
            )
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        DateSection()
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(visitedLocations.size) {
                TripDetailsCard(visitedLocations[it])
            }
        }
    }
}

@Preview
@Composable
fun TripDetailsPreview() {
    TravelAssistantTheme {
        TripDetailsScreen(
            onNavigateBack = {}, onComposing = {})
    }
}