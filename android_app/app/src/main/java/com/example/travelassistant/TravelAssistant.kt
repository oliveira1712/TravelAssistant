package com.example.travelassistant

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.travelassistant.models.navigation.AppBarState
import com.example.travelassistant.ui.navigation.BottomNavigationBar
import com.example.travelassistant.ui.navigation.Navigations
import com.example.travelassistant.ui.navigation.TopBar
import com.example.travelassistant.ui.shared.components.ConnectivityAlert
import com.example.travelassistant.utils.createNotificationChannel
import com.example.travelassistant.viewmodels.TravelAssistantViewModel

@Composable
fun TravelAssistant(windowSize: WindowWidthSizeClass) {
    val context = LocalContext.current

    val navController = rememberNavController()
    var appBarState by remember {
        mutableStateOf(AppBarState())
    }

    val travelAssistantViewModel: TravelAssistantViewModel = viewModel()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    //Comparison with null because if it's null there is no other screens on the stack, so
    //it's the "blank screen"
    val isCurrentRouteSplashScreen =
        navBackStackEntry?.destination?.route == null || navBackStackEntry?.destination?.route == "splashScreen"
    val isUserLoggedIn = travelAssistantViewModel.getIsUserLoggedIn().collectAsState(false)
    val isNavigationsVisible = isUserLoggedIn.value && !isCurrentRouteSplashScreen

    LaunchedEffect(Unit) {
        createNotificationChannel("TravelAssistantChannel", context)
    }

    Scaffold(
        topBar = { if (isNavigationsVisible) TopBar(appBarState) },
        bottomBar = { if (isNavigationsVisible) BottomNavigationBar(navController) },
        content = { padding ->
            Box(modifier = Modifier.padding(padding)) {
                Navigations(
                    changeAppBarState = {
                        appBarState = it
                    },
                    navController = navController,
                    travelAssistantViewModel = travelAssistantViewModel,
                    windowSize = windowSize
                )

                if (isNavigationsVisible) ConnectivityAlert()
            }
        },
        backgroundColor = MaterialTheme.colors.background // Set background color to avoid the white flashing when you switch between screens
    )
}

@Preview(showBackground = true)
@Composable
fun TravelAssistantPreview() {
    TravelAssistant(WindowWidthSizeClass.Compact)
}