package com.example.travelassistant.ui.screens.map

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddRoad
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.travelassistant.R
import com.example.travelassistant.models.navigation.AppBarState
import com.example.travelassistant.sensors.locationPermissions
import com.example.travelassistant.services.LocationService
import com.example.travelassistant.ui.screens.interests.InterestsScreen
import com.example.travelassistant.ui.screens.login.LoginScreen
import com.example.travelassistant.viewmodels.TravelAssistantViewModel

import com.example.travelassistant.ui.shared.components.DialogCreateRoute
import com.example.travelassistant.ui.theme.TravelAssistantTheme

@Composable
fun MapScreen(onComposing: (AppBarState) -> Unit) {
    val screenName = stringResource(id = R.string.map_screen)
    val mContext = LocalContext.current
    val mapViewModel: MapViewModel = viewModel(mContext as ComponentActivity)
    val travelAssistantViewModel: TravelAssistantViewModel = viewModel()
    val navigationType = travelAssistantViewModel.getNavigationType().collectAsState("")
    var showRouteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        onComposing(AppBarState(title = screenName))
    }

    if (locationPermissions() == 2) {
        //Start updating user location
        mapViewModel.startLocationUpdates()

        //Start the foreground service
        Intent(mContext, LocationService::class.java).apply {
            action = LocationService.ACTION_START
            mContext.startService(this)
        }
        DrawMap(navigationType = navigationType.value)

        Box(modifier = Modifier.fillMaxSize()) {
            if (locationPermissions() == 2) {
                mapViewModel.startLocationUpdates()
                DrawMap(navigationType = navigationType.value)
                FloatingActionButton(
                    onClick = { showRouteDialog = true },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 70.dp),
                    backgroundColor = MaterialTheme.colors.primary
                )
                {
                    Icon(
                        imageVector = Icons.Filled.AddRoad,
                        contentDescription = "Add Route",
                        tint = Color.White
                    )
                }

                if (showRouteDialog) {
                    DialogCreateRoute(onDismiss = { showRouteDialog = !showRouteDialog })
                }
            }
        }
    }
}


@Preview
@Composable
fun MapScreenPreview() {
    var appBarState by remember {
        mutableStateOf(AppBarState())
    }

    TravelAssistantTheme {
        MapScreen{
            appBarState = it
        }
    }
}