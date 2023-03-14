package com.example.travelassistant.ui.navigation

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.travelassistant.models.navigation.AppBarState
import com.example.travelassistant.models.navigation.NavigationItem
import com.example.travelassistant.ui.screens.gasStations.GasStationsScreen
import com.example.travelassistant.ui.screens.interests.InterestsScreen
import com.example.travelassistant.ui.screens.interests.SavedInterestsScreen
import com.example.travelassistant.ui.screens.login.LoginScreen
import com.example.travelassistant.ui.screens.map.MapScreen
import com.example.travelassistant.ui.screens.profile.ProfileScreen
import com.example.travelassistant.ui.screens.register.RegisterScreen
import com.example.travelassistant.ui.screens.routes.RoutesScreen
import com.example.travelassistant.ui.screens.more.MoreScreen
import com.example.travelassistant.ui.screens.splashScreen.SplashScreen
import com.example.travelassistant.ui.screens.tripDetails.TripDetailsScreen
import com.example.travelassistant.ui.screens.vehicles.InsertVehicles
import com.example.travelassistant.ui.screens.vehicles.VehiclesScreen
import com.example.travelassistant.viewmodels.TravelAssistantViewModel

@Composable
fun Navigations(
    changeAppBarState: (appBarState: AppBarState) -> Unit,
    navController: NavHostController,
    travelAssistantViewModel: TravelAssistantViewModel,
    windowSize: WindowWidthSizeClass,
) {
    NavHost(navController, startDestination = "splashScreen") {
        composable(NavigationItem.Map.route) {
            MapScreen {
                changeAppBarState(it)
            }
        }
        composable(NavigationItem.Interests.route) {
            InterestsScreen(onNavigateToMap = { navController.navigate("map") { popUpTo(0) } }) {
                changeAppBarState(it)
            }
        }
        composable(NavigationItem.More.route) {
            MoreScreen(
                onNavigateToMap = { navController.navigate("map") },
                onNavigateToVehicles = { navController.navigate("vehicles") },
                onNavigateToSavedInterests = { navController.navigate("savedInterests") },
                onNavigateToLogin = { navController.navigate("login") },
                onNavigateToUserProfile = { navController.navigate("userProfile") },
                onNavigateToRoutes = { navController.navigate("routes") },
                onNavigateToTripDetails = { navController.navigate("tripDetails") }) {
                changeAppBarState(it)
            }
        }

        composable(NavigationItem.Stations.route) {
            GasStationsScreen(onNavigateToMap = { navController.navigate("map") }, windowSize = windowSize) {
                changeAppBarState(it)
            }
        }

        composable("splashScreen") {
            SplashScreen(onNavigateToLogin = { navController.navigate("login") },
                onNavigateToMap = { navController.navigate("map") { popUpTo(0) } })
        }
        composable("login") {
            LoginScreen(travelAssistantViewModel = travelAssistantViewModel,
                onNavigateToMap = { navController.navigate("map") { popUpTo(0) } },
                onNavigateToRegister = { navController.navigate("register") })
        }
        composable("register") {
            RegisterScreen(travelAssistantViewModel = travelAssistantViewModel,
                onNavigateToMap = { navController.navigate("map") { popUpTo(0) } },
                onNavigateToLogin = { navController.navigate("login") })
        }
        composable("vehicles") {
            VehiclesScreen(onNavigateToInsertVehicle = { navController.navigate("insertVehicle") },
                onNavigateBack = { navController.popBackStack() }) {
                changeAppBarState(it)
            }
        }
        composable("insertVehicle") {
            InsertVehicles(onNavigateBack = { navController.popBackStack() }) {
                changeAppBarState(it)
            }
        }
        composable("savedInterests") {
            SavedInterestsScreen(onNavigateToMap = { navController.navigate("map") }, onNavigateBack = { navController.popBackStack() }) {
                changeAppBarState(it)
            }
        }
        composable("userProfile") {
            ProfileScreen(onNavigateBack = { navController.popBackStack() }) {
                changeAppBarState(it)
            }
        }
        composable("routes") {
            RoutesScreen(onNavigateToMap = { navController.navigate("map") }, onNavigateBack = { navController.popBackStack() }) {
                changeAppBarState(it)
            }
        }
        composable("tripDetails") {
            TripDetailsScreen(onNavigateBack = { navController.popBackStack() }) {
                changeAppBarState(it)
            }
        }
    }
}
