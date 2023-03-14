package com.example.travelassistant.ui.screens.vehicles.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.travelassistant.R
import com.example.travelassistant.models.Vehicle
import com.example.travelassistant.ui.screens.tripDetails.components.DateSection
import com.example.travelassistant.ui.screens.vehicles.VehiclesViewModel
import com.example.travelassistant.ui.shared.components.MyButton
import com.example.travelassistant.ui.theme.Green700
import com.example.travelassistant.ui.theme.TravelAssistantTheme
import com.example.travelassistant.viewmodels.TravelAssistantViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun VehicleCard(
    vehicle: Vehicle, selectedVehicle: String
) {
    var showCustomDialog by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .padding(start = 10.dp, end = 10.dp)
            .height(200.dp)
            .fillMaxWidth()
    ) {
        Card(modifier = Modifier.height(160.dp),
            shape = RoundedCornerShape(10.dp),
            elevation = 10.dp,
            onClick = {
                showCustomDialog = !showCustomDialog
            }) {
            Box(
                modifier = Modifier.fillMaxWidth(),
            ) {
                AsyncImage(
                    model =
                    ImageRequest.Builder(LocalContext.current)
                        .data(vehicle.imageUrl)
                        .crossfade(true).build(),
                    contentDescription = "vehicle image",
                    contentScale = ContentScale.Crop,
                )

                Surface(
                    color = Color.Black.copy(alpha = 0.45f), modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp),
                        textAlign = TextAlign.Left,
                        text = vehicle.name,
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.ExtraBold,
                        )
                    )
                }
            }
        }
        if (showCustomDialog) {
            VehicleDialog({ showCustomDialog = !showCustomDialog }, vehicle, selectedVehicle)
        }

        if (vehicle.licencePlate == selectedVehicle) {
            FloatingActionButton(
                modifier = Modifier
                    .offset(y = -15.dp)
                    .align(Alignment.TopEnd)
                    .padding(0.dp, 0.dp),
                onClick = {

                },
                backgroundColor = Green700,
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.NoCrash, "")
            }
        }
    }
}

@Preview
@Composable
fun VehicleCardPreview() {
    TravelAssistantTheme {
        val vehicle = Vehicle(licencePlate = "", userEmail = "bruno@gmail.com", brand = "", model = "", name = "", kms = 90000.0, imageUrl = "")
        VehicleCard(vehicle = vehicle, selectedVehicle = "")
    }
}

@Composable
fun VehicleDialog(onDismiss: () -> Unit, vehicle: Vehicle, savedVehicleLicensePlate: String) {
    val vehiclesViewModels: VehiclesViewModel = viewModel()
    val travelAssistantViewModel: TravelAssistantViewModel = viewModel()

    Dialog(
        onDismissRequest = { onDismiss() }, properties = DialogProperties(
            dismissOnBackPress = true, dismissOnClickOutside = true
        )
    ) {
        Card(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            elevation = 8.dp
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.background)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(MaterialTheme.colors.primary.copy(alpha = 0.6F)),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,

                    ) {

                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current).data(vehicle.imageUrl)
                            .crossfade(true).build(),
                        contentDescription = "",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Text(
                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp),
                    textAlign = TextAlign.Left,
                    text = vehicle.name,
                    style = TextStyle(
                        color = MaterialTheme.colors.onBackground,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                    )
                )

                VehicleDialogInfoLine(label = "Car Brand: ", info = vehicle.brand)

                VehicleDialogInfoLine(label = "Car Model: ", info = vehicle.model)

                VehicleDialogInfoLine(label = "Kilometers: ", info = vehicle.kms.toString())

                if (vehicle.licencePlate != savedVehicleLicensePlate) {
                    MyButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        buttonColors = ButtonDefaults.buttonColors(
                            MaterialTheme.colors.primary,
                            Color.White
                        ),
                        label = stringResource(id = R.string.select_car_text),
                        contentPadding = PaddingValues(all = 2.dp),
                        onClick = {
                            travelAssistantViewModel.setSelectedVehicle(vehicle.licencePlate)
                            onDismiss()
                        }
                    )
                }

                MyButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    buttonColors = ButtonDefaults.buttonColors(Color.Red, Color.White),
                    label = stringResource(id = R.string.remove_car_text),
                    contentPadding = PaddingValues(all = 2.dp),
                    onClick = {
                        vehiclesViewModels.deleteVehicleFromFirebaseDB(vehicle)
                        onDismiss()
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun VehicleDialogPreview() {
    TravelAssistantTheme {
        val vehicle = Vehicle(licencePlate = "", userEmail = "bruno010602@gmail.com", brand = "", model = "", name = "", kms = 90000.0, imageUrl = "")
        VehicleDialog(onDismiss = {}, vehicle = vehicle, savedVehicleLicensePlate = "")
    }
}

@Composable
fun VehicleDialogInfoLine(label: String, info: String){
    Row(modifier = Modifier.padding(top = 10.dp)) {
        Text(
            modifier = Modifier.padding(start = 10.dp, end = 3.dp),
            textAlign = TextAlign.Left,
            text = label,
            style = TextStyle(
                color = MaterialTheme.colors.onBackground,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
            )
        )
        Text(
            textAlign = TextAlign.Left,
            text = info,
            style = TextStyle(
                color = MaterialTheme.colors.onBackground,
                fontSize = 16.sp,
            )
        )
    }
}

@Preview
@Composable
fun VehicleDialogInfoLinePreview() {
    TravelAssistantTheme {
        VehicleDialogInfoLine(label = "", info = "")
    }
}

