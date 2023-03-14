package com.example.travelassistant.ui.screens.interests

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travelassistant.R
import com.example.travelassistant.models.navigation.AppBarState
import com.example.travelassistant.ui.screens.gasStations.GasStationsScreen
import com.example.travelassistant.ui.screens.interests.components.MySavedInterestCard
import com.example.travelassistant.ui.theme.TravelAssistantTheme

@Composable
fun SavedInterestsScreen(onNavigateToMap: () -> Unit, onNavigateBack: () -> Unit, onComposing: (AppBarState) -> Unit) {
    val interestsViewModel: InterestsViewModel = viewModel()
    val savedInterests = interestsViewModel.allSavedInterests.observeAsState(emptyList())
    val screenName = stringResource(id = R.string.saved_interest_screen)

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

    LazyColumn {
        items(savedInterests.value) {
            println(it.name)
            MySavedInterestCard(onNavigateToMap, it)
        }
    }
}

@Preview
@Composable
fun SavedInterestsScreenPreview() {
    var appBarState by remember {
        mutableStateOf(AppBarState())
    }

    TravelAssistantTheme {
        SavedInterestsScreen({}, onNavigateBack = {}) {
            appBarState = it
        }
    }
}