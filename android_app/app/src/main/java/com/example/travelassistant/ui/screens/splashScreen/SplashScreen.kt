package com.example.travelassistant.ui.screens.splashScreen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travelassistant.models.Districts.DistrictResult
import com.example.travelassistant.models.Municipios.MunicipiosResult
import com.example.travelassistant.models.Route
import com.example.travelassistant.models.Vehicle
import com.example.travelassistant.models.gasStations.FuelType.FuelTypeResult
import com.example.travelassistant.models.gasStations.GasStationResult
import com.example.travelassistant.ui.screens.gasStations.GasStationsViewModel
import com.example.travelassistant.ui.screens.routes.RoutesViewModel
import com.example.travelassistant.ui.screens.splashScreen.components.Splash
import com.example.travelassistant.ui.screens.vehicles.VehiclesViewModel
import com.example.travelassistant.viewmodels.TravelAssistantViewModel
import kotlinx.coroutines.delay

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SplashScreen(onNavigateToLogin: () -> Unit, onNavigateToMap: () -> Unit) {
    val gasStationsViewModel: GasStationsViewModel = viewModel()
    val travelAssistantViewModel: TravelAssistantViewModel = viewModel()
    val districtsList = gasStationsViewModel.districts.value?.data?.resultado
    val municipiosList = gasStationsViewModel.municipios.value?.data?.resultado
    val fuelTypesList = gasStationsViewModel.fuelTypes.value?.data?.resultado
    val isUserLoggedIn = travelAssistantViewModel.getIsUserLoggedIn().collectAsState(false)

    LaunchedEffect(key1 = true) {
        makeAPICall(gasStationsViewModel)
        delay(3000)

        if (isUserLoggedIn.value) {
            onNavigateToMap()
        } else {
            onNavigateToLogin()
        }
    }

    Splash()

    loadDistricts(districtsList, gasStationsViewModel)
    loadMunicipios(municipiosList, gasStationsViewModel)
    loadFuelTypes(fuelTypesList, gasStationsViewModel)

}

private fun loadDistricts(
    districtsList: List<DistrictResult>?, gasStationsViewModel: GasStationsViewModel
): Boolean {
    districtsList?.forEach { it ->
        gasStationsViewModel.insertDistrictIntoDB(
            DistrictResult(
                it.Descritivo, it.Id
            )
        )
    }

    return true
}

private fun loadMunicipios(
    municipiosList: List<MunicipiosResult>?, gasStationsViewModel: GasStationsViewModel
): Boolean {
    municipiosList?.forEach() { it ->
        gasStationsViewModel.insertMunicipioIntoDB(
            MunicipiosResult(
                it.Descritivo,
                it.IdDistrito,
                it.Id,
            )
        )
    }

    return true
}

private fun loadFuelTypes(
    fuelTypesList: List<FuelTypeResult>?, gasStationsViewModel: GasStationsViewModel
): Boolean {
    fuelTypesList?.forEach() { it ->
        gasStationsViewModel.insertFuelTypeIntoDB(
            FuelTypeResult(
                it.Id, it.Descritivo
            )
        )
    }

    return true
}

private fun makeAPICall(gasStationsViewModel: GasStationsViewModel) {
    /**
     * Os parâmetros são enviados vazios no âmbito de descarregar todas as bombas do país e não
     * apenas as de um distrito/municipio e tipo de combustiível específico
     */
    gasStationsViewModel.getDistrictsFromAPI()
    gasStationsViewModel.getMunicipiosFromAPI()
    gasStationsViewModel.getFuelTypesFromAPI()
}
