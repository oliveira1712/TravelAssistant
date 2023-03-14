@file:OptIn(ExperimentalMaterialApi::class)

package com.example.travelassistant.ui.screens.gasStations.components

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travelassistant.models.gasStations.GasStationResult
import com.example.travelassistant.models.gasStations.GasStationsFilters
import com.example.travelassistant.models.googledirections.*
import com.example.travelassistant.ui.screens.gasStations.GasStationsViewModel
import com.example.travelassistant.ui.screens.map.MapViewModel
import com.example.travelassistant.ui.theme.*
import com.example.travelassistant.utils.startNavigation
import com.example.travelassistant.viewmodels.TravelAssistantViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate


@Composable
fun DistrictsSelectBox(
    gasStationsViewModel: GasStationsViewModel, defaultValue: String
) {
    var selectedItem by remember {
        mutableStateOf(defaultValue)
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    val districts = gasStationsViewModel.allDistricts.observeAsState()

    ExposedDropdownMenuBox(modifier = Modifier
        .padding(vertical = 5.dp)
        .fillMaxWidth(),
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }) {
        TextField(value = selectedItem, onValueChange = {}, readOnly = true, trailingIcon = {
            ExposedDropdownMenuDefaults.TrailingIcon(
                expanded = expanded
            )
        }, colors = ExposedDropdownMenuDefaults.textFieldColors()
        )

        val coroutineScope = rememberCoroutineScope()

        // menu
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            districts.value?.let {
                it.forEach() { district ->
                    DropdownMenuItem(onClick = {
                        selectedItem = district.Descritivo
                        coroutineScope.launch {
                            gasStationsViewModel.selectDistrict(district = district)
                        }
                        expanded = false
                    }) {
                        Text(text = district.Descritivo)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun DistrictsSelectBoxPreview() {
    val gasStationsViewModel: GasStationsViewModel = viewModel()
    val defaultValue = ""

    TravelAssistantTheme {
        DistrictsSelectBox(
            gasStationsViewModel = gasStationsViewModel, defaultValue = defaultValue
        )
    }
}

@Composable
fun MunicipiosSelectBox(
    gasStationsViewModel: GasStationsViewModel, defaultValue: String
) {
    var selectedItem by remember {
        mutableStateOf(defaultValue)
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    val municipios = gasStationsViewModel.municipiosBySelectedDistrict.observeAsState()
    val coroutineScope = rememberCoroutineScope()

    ExposedDropdownMenuBox(modifier = Modifier
        .padding(vertical = 5.dp)
        .fillMaxWidth(),
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }) {
        TextField(value = selectedItem, onValueChange = {}, readOnly = true, trailingIcon = {
            ExposedDropdownMenuDefaults.TrailingIcon(
                expanded = expanded
            )
        }, colors = ExposedDropdownMenuDefaults.textFieldColors()
        )

        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            municipios.value?.let {
                it.forEach() { municipio ->
                    DropdownMenuItem(onClick = {
                        selectedItem = municipio.Descritivo
                        coroutineScope.launch {
                            gasStationsViewModel.selectMunicipio(municipio = municipio)
                        }
                        expanded = false
                    }) {
                        Text(text = municipio.Descritivo)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun MunicipiosSelectBoxPreview() {
    val gasStationsViewModel: GasStationsViewModel = viewModel()
    val defaultValue = ""

    TravelAssistantTheme {
        MunicipiosSelectBox(
            gasStationsViewModel = gasStationsViewModel, defaultValue = defaultValue
        )
    }
}

@Composable
fun FuelTypeSelectBox(
    gasStationsViewModel: GasStationsViewModel, defaultValue: String
) {
    var selectedItem by remember {
        mutableStateOf(defaultValue)
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    val fuelTypes = gasStationsViewModel.getFuelTypesFromDB().observeAsState()
    val coroutineScope = rememberCoroutineScope()

    ExposedDropdownMenuBox(modifier = Modifier
        .padding(vertical = 5.dp)
        .fillMaxWidth(),
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }) {
        TextField(value = selectedItem, onValueChange = {}, readOnly = true, trailingIcon = {
            ExposedDropdownMenuDefaults.TrailingIcon(
                expanded = expanded
            )
        }, colors = ExposedDropdownMenuDefaults.textFieldColors()
        )

        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            fuelTypes.value?.let {
                it.forEach() { fuelType ->
                    DropdownMenuItem(onClick = {
                        selectedItem = fuelType.Descritivo
                        coroutineScope.launch {
                            gasStationsViewModel.selectFuelType(fuelType = fuelType)
                        }
                        expanded = false
                    }) {
                        Text(text = fuelType.Descritivo)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun FuelTypeSelectBoxPreview() {
    val gasStationsViewModel: GasStationsViewModel = viewModel()
    val defaultValue = ""

    TravelAssistantTheme {
        FuelTypeSelectBox(
            gasStationsViewModel = gasStationsViewModel, defaultValue = defaultValue
        )
    }
}


@Composable
fun GasStationItem(onNavigateToMap: () -> Unit, gasStation: GasStationResult, onClick: () -> Unit) {
    var showGasStationCustomDialog by remember { mutableStateOf(false) }
    val mapViewModel: MapViewModel = viewModel(LocalContext.current as ComponentActivity)
    val travelAssistantViewModel: TravelAssistantViewModel = viewModel()
    val navigationType = travelAssistantViewModel.getNavigationType().collectAsState("")
    val mContext = LocalContext.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 10.dp, end = 5.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.LocalGasStation,
            contentDescription = "",
            tint = if (gasStation.Combustivel == "Gasóleo especial") TopDieselYellow
            else if (gasStation.Combustivel == "Gasóleo simples") Color.Black
            else if (gasStation.Combustivel == "Gasolina 98") Green700
            else if (gasStation.Combustivel == "Gasolina Simples 95" || gasStation.Combustivel == "Gasolina Especial 95") Green500
            else Color.LightGray,
            modifier = Modifier
                .padding(start = 5.dp, end = 8.dp)
                .size(40.dp)
        )
        Column(modifier = Modifier
            .padding(start = 10.dp)
            .weight(0.9f)
            .clickable {
                showGasStationCustomDialog = true
            }) {
            Text(
                text = gasStation.Nome,
                fontSize = 18.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Text(
                text = "preço p/litro: " + gasStation.Preco,
                fontSize = 14.sp,
                color = Color.Gray,
            )
        }
        Spacer(Modifier.weight(0.1f))

        Icon(imageVector = Icons.Filled.Directions,
            contentDescription = "",
            tint = DirectionsBlue,
            modifier = Modifier
                .padding(end = 8.dp)
                .size(30.dp)
                .clickable {
                    val routeOptions = DirectionRequestBody(
                        destination = Destination(
                            Location(
                                LatLngDirections(
                                    gasStation.Latitude, gasStation.Longitude
                                )
                            )
                        ),
                        routeModifiers = RouteModifiers(avoidHighways = true, avoidTolls = true),
                    )

                    startNavigation(
                        context = mContext,
                        mapViewModel = mapViewModel,
                        routeOptions = routeOptions,
                        navigationType = navigationType.value,
                        onNavigateToMap = onNavigateToMap
                    )
                })
    }
    Divider(
        startIndent = 10.dp,
        thickness = 1.dp,
        color = if (MaterialTheme.colors.isLight) Color.LightGray else Color.DarkGray,
        modifier = Modifier.padding(start = 6.dp, end = 10.dp, bottom = 10.dp, top = 10.dp)
    )

    if (showGasStationCustomDialog) {
        GasStationItemCustomDialog(
            onDismiss = { showGasStationCustomDialog = !showGasStationCustomDialog },
            onNegativeClick = { showGasStationCustomDialog = !showGasStationCustomDialog },
            onPositiveClick = {},
            gasStation = gasStation
        )
    }
}

@Preview
@Composable
fun GasStationItemPreview() {
    val gasStation = GasStationResult(
        Id = "88827",
        Nome = "LOOPENERGY LDA OIA",
        TipoPosto = "Outro",
        Municipio = "Oliveira do Bairro",
        Preco = "1,264 €",
        Marca = "PRIO",
        Combustivel = "Gasóleo colorido",
        Distrito = "Aveiro",
        Morada = "R. 30 de Junho, Posto de Abastecimento OIA",
        Localidade = "OIA",
        Latitude = 40.54418,
        Longitude = -8.53602,
        LocalDate.now().toString()
    )

    TravelAssistantTheme {
        GasStationItem(
            {},
            gasStation = gasStation,
            onClick = {}
        )
    }
}

@Composable
fun GasStationFiltersCustomDialog(
    onDismiss: () -> Unit,
    onNegativeClick: () -> Unit,
    onPositiveClick: (GasStationsFilters) -> Unit,
    gasStationsViewModel: GasStationsViewModel,
    filters: GasStationsFilters
) {
    val mContext = LocalContext.current

    Dialog(onDismissRequest = onDismiss) {
        Card(
            elevation = 8.dp, shape = RoundedCornerShape(12.dp)
        ) {
            Column {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        text = "Filters",
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {

                        Column() {
                            Text(
                                modifier = Modifier.padding(bottom = 5.dp),
                                fontWeight = FontWeight.Medium,
                                fontSize = 17.sp,
                                text = "District"
                            )

                            DistrictsSelectBox(
                                gasStationsViewModel = gasStationsViewModel,
                                defaultValue = filters.district
                            )

                            Text(
                                modifier = Modifier.padding(vertical = 5.dp),
                                fontWeight = FontWeight.Medium,
                                fontSize = 17.sp,
                                text = "Municipios"
                            )

                            MunicipiosSelectBox(
                                gasStationsViewModel = gasStationsViewModel,
                                defaultValue = filters.county
                            )

                            Text(
                                modifier = Modifier.padding(vertical = 5.dp),
                                fontWeight = FontWeight.Medium,
                                fontSize = 17.sp,
                                text = "Fuel Type"
                            )

                            FuelTypeSelectBox(
                                gasStationsViewModel = gasStationsViewModel,
                                defaultValue = filters.fuelType
                            )
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                    ) {


                        Button(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.background,
                                contentColor = MaterialTheme.colors.primary,

                                ),
                            elevation = null,
                            border = BorderStroke(
                                1.dp,
                                if (MaterialTheme.colors.isLight) Color.LightGray else Color.DarkGray
                            ),
                            onClick = onNegativeClick
                        ) {
                            Text(text = "Cancel")
                        }
                        Spacer(modifier = Modifier.size(10.dp))

                        Button(modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            onClick = {
                                if (gasStationsViewModel.selectedDistrict.value != null
                                    && gasStationsViewModel.selectedMunicipio.value != null
                                    && gasStationsViewModel.selectedFuelType.value != null
                                ) {
                                    onPositiveClick(
                                        GasStationsFilters(
                                            gasStationsViewModel.selectedDistrict.value!!.Descritivo,
                                            gasStationsViewModel.selectedMunicipio.value!!.Descritivo,
                                            gasStationsViewModel.selectedFuelType.value!!.Descritivo
                                        )
                                    )
                                } else {
                                    Toast.makeText(
                                        mContext,
                                        "Select the fields",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }) {
                            Text(text = "Apply", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun GasStationFiltersCustomDialogPreview() {
    val gasStationsViewModel: GasStationsViewModel = viewModel()
    val filters = GasStationsFilters()

    TravelAssistantTheme {
        GasStationFiltersCustomDialog(
            onDismiss = {},
            onNegativeClick = {},
            onPositiveClick = {},
            gasStationsViewModel = gasStationsViewModel,
            filters = filters
        )
    }
}

@Composable
fun GasStationFiltersBigScreens(
    onPositiveClick: (GasStationsFilters) -> Unit,
    gasStationsViewModel: GasStationsViewModel,
    filters: GasStationsFilters
) {
    val mContext = LocalContext.current
    Column {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = "Filters",
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                Column() {
                    Text(
                        modifier = Modifier.padding(bottom = 5.dp),
                        fontWeight = FontWeight.Medium,
                        fontSize = 17.sp,
                        text = "District"
                    )

                    DistrictsSelectBox(
                        gasStationsViewModel = gasStationsViewModel, defaultValue = filters.district
                    )

                    Text(
                        modifier = Modifier.padding(vertical = 5.dp),
                        fontWeight = FontWeight.Medium,
                        fontSize = 17.sp,
                        text = "Municipios"
                    )

                    MunicipiosSelectBox(
                        gasStationsViewModel = gasStationsViewModel, defaultValue = filters.county
                    )

                    Text(
                        modifier = Modifier.padding(vertical = 5.dp),
                        fontWeight = FontWeight.Medium,
                        fontSize = 17.sp,
                        text = "Fuel Type"
                    )

                    FuelTypeSelectBox(
                        gasStationsViewModel = gasStationsViewModel, defaultValue = filters.fuelType
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            ) {
                Button(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    onClick = {
                        if (gasStationsViewModel.selectedDistrict.value != null
                            && gasStationsViewModel.selectedMunicipio.value != null
                            && gasStationsViewModel.selectedFuelType.value != null
                        ) {
                            onPositiveClick(
                                GasStationsFilters(
                                    gasStationsViewModel.selectedDistrict.value!!.Descritivo,
                                    gasStationsViewModel.selectedMunicipio.value!!.Descritivo,
                                    gasStationsViewModel.selectedFuelType.value!!.Descritivo
                                )
                            )
                        } else {
                            Toast.makeText(mContext, "Select the fields", Toast.LENGTH_SHORT).show()
                        }

                    }) {
                    Text(text = "Apply", color = Color.White)
                }
            }
        }
    }
}

@Preview
@Composable
fun GasStationFiltersBigScreensPreview() {
    val gasStationsViewModel: GasStationsViewModel = viewModel()
    val filters = GasStationsFilters()

    TravelAssistantTheme {
        GasStationFiltersBigScreens(
            onPositiveClick = {},
            gasStationsViewModel = gasStationsViewModel,
            filters = filters
        )
    }
}

@Composable
fun GasStationInfoLine(label: String, info: String) {
    Row() {
        Text(
            modifier = Modifier.padding(bottom = 5.dp, end = 2.dp),
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            text = label
        )
        Text(
            modifier = Modifier.padding(bottom = 5.dp),
            fontWeight = FontWeight.Normal,
            fontSize = 17.sp,
            text = info
        )
    }
}

@Preview
@Composable
fun GasStationInfoLine() {
    TravelAssistantTheme {
        GasStationInfoLine(label = "Teste", info = "Teste")
    }
}


@Composable
fun GasStationItemCustomDialog(
    onDismiss: () -> Unit,
    onNegativeClick: () -> Unit,
    onPositiveClick: (GasStationsFilters) -> Unit,
    gasStation: GasStationResult,
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            elevation = 8.dp, shape = RoundedCornerShape(12.dp)
        ) {
            Column {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        text = gasStation.Nome,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        fontSize = 22.sp,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {

                        Column() {
                            GasStationInfoLine(label = "Tipo Posto: ", info = gasStation.TipoPosto)
                            GasStationInfoLine(label = "Marca: ", info = gasStation.Marca)
                            GasStationInfoLine(
                                label = "Combustivel: ", info = gasStation.Combustivel
                            )
                            GasStationInfoLine(
                                label = "Localização: ", info = gasStation.Morada
                            )
                        }
                    }
                    // Buttons
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                    ) {

                        Button(modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            onClick = {

                            }) {
                            Text(text = "Add as favourite", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun GasStationItemCustomDialogPreview() {
    TravelAssistantTheme {
        val gasStation = GasStationResult(
            Id = "88827",
            Nome = "LOOPENERGY LDA OIA",
            TipoPosto = "Outro",
            Municipio = "Oliveira do Bairro",
            Preco = "1,264 €",
            Marca = "PRIO",
            Combustivel = "Gasóleo colorido",
            Distrito = "Aveiro",
            Morada = "R. 30 de Junho, Posto de Abastecimento OIA",
            Localidade = "OIA",
            Latitude = 40.54418,
            Longitude = -8.53602,
            LocalDate.now().toString()
        )

        GasStationItemCustomDialog(
            onDismiss = {},
            onNegativeClick = {},
            onPositiveClick = {},
            gasStation = gasStation,
        )
    }
}
