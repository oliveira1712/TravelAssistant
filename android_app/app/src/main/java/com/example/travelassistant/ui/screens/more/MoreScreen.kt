package com.example.travelassistant.ui.screens.more

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travelassistant.R
import com.example.travelassistant.models.navigation.AppBarState
import com.example.travelassistant.ui.screens.more.components.*
import com.example.travelassistant.ui.theme.TravelAssistantTheme
import com.example.travelassistant.viewmodels.TravelAssistantViewModel


@Composable
fun MoreScreen(
    onNavigateToMap: () -> Unit,
    onNavigateToVehicles: () -> Unit,
    onNavigateToSavedInterests: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToUserProfile: () -> Unit,
    onNavigateToRoutes: () -> Unit,
    onNavigateToTripDetails: () -> Unit,
    onComposing: (AppBarState) -> Unit
) {
    val screenName = stringResource(id = R.string.more_screen)

    val travelAssistantViewModel: TravelAssistantViewModel = viewModel()

    val lightThemeString = stringResource(id = R.string.theme_mode_light)
    val darkThemeString = stringResource(id = R.string.theme_mode_dark)
    var showThemeDialog by remember { mutableStateOf(false) }
    val savedTheme = travelAssistantViewModel.getSavedTheme().collectAsState("")

    val travelAssistantTypeString = stringResource(id = R.string.navigation_mode_travelassistant)
    val googleMapsTypeString = stringResource(id = R.string.navigation_mode_googlemaps)
    var showNavigationDialog by remember { mutableStateOf(false) }
    val savedNavigationType = travelAssistantViewModel.getNavigationType().collectAsState("")

    LaunchedEffect(key1 = true) {
        onComposing(
            AppBarState(
                title = screenName,
            )
        )
    }

    if (showThemeDialog) {
        ThemeCustomDialog(theme = savedTheme.value, onDismiss = {
            showThemeDialog = !showThemeDialog
        }, onNegativeClick = {
            showThemeDialog = !showThemeDialog
        }, onPositiveClick = { themeSelected ->
            val themeDataStore = when (themeSelected) {
                darkThemeString -> "Dark"
                lightThemeString -> "Light"
                else -> "System Preferences"
            }

            travelAssistantViewModel.setTheme(themeDataStore)

            showThemeDialog = !showThemeDialog
        })
    }

    if (showNavigationDialog) {
        NavigationPreferencesDialog(navigationType = savedNavigationType.value, onDismiss = {
            showNavigationDialog = !showNavigationDialog
        }, onNegativeClick = {
            showNavigationDialog = !showNavigationDialog
        }, onPositiveClick = { navigationTypeSelected ->
            val navigationTypeDataStore = when (navigationTypeSelected) {
                travelAssistantTypeString -> "TravelAssistant"
                googleMapsTypeString -> "GoogleMaps"
                else -> "TravelAssistant"
            }

            travelAssistantViewModel.setNavigationType(navigationTypeDataStore)

            showNavigationDialog = !showNavigationDialog
        })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        ProfileCardUI(onNavigateToUserProfile)
        GeneralOptionsUI(onOpenThemeDialog = {
            showThemeDialog = true
        }, onOpenNavigationDialog = {
            showNavigationDialog = true
        })
        OtherOptionsUI(
            onNavigateToVehicles = onNavigateToVehicles,
            onNavigateToSavedInterests = onNavigateToSavedInterests,
            onNavigateToLogin = onNavigateToLogin,
            onNavigateToRoutes = onNavigateToRoutes,
            onNavigateToTripDetails = onNavigateToTripDetails
        )
    }
}

@Preview
@Composable
fun MoreScreenPreview() {
    TravelAssistantTheme {
        MoreScreen(
            onNavigateToMap = {},
            onNavigateToVehicles = {},
            onNavigateToSavedInterests = {},
            onNavigateToLogin = {},
            onNavigateToUserProfile = {},
            onNavigateToRoutes = {},
            onNavigateToTripDetails = {},
            onComposing = {})
    }
}

