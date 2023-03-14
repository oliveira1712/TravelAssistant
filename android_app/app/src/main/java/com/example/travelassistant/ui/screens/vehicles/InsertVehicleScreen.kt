package com.example.travelassistant.ui.screens.vehicles

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travelassistant.R
import com.example.travelassistant.models.Vehicle
import com.example.travelassistant.models.navigation.AppBarState
import com.example.travelassistant.ui.screens.vehicles.components.VehicleDialogInfoLine
import com.example.travelassistant.ui.shared.components.ImagePicker
import com.example.travelassistant.ui.shared.components.MyButton
import com.example.travelassistant.ui.shared.components.MyOutlinedInputText
import com.example.travelassistant.ui.theme.TravelAssistantTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.DelicateCoroutinesApi

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun InsertVehicles(onNavigateBack: () -> Unit, onComposing: (AppBarState) -> Unit) {
    val screenName = stringResource(id = R.string.insert_vehicles_screen)

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

    var vehicleLicensePlate by remember { mutableStateOf("") }
    var vehicleName by remember { mutableStateOf("") }
    var vehicleBrand by remember { mutableStateOf("") }
    var vehicleModel by remember { mutableStateOf("") }
    var vehicleKms by remember { mutableStateOf("") }
    var invalidForm by remember { mutableStateOf(false) }

    val vehiclesViewModels: VehiclesViewModel = viewModel()
    val fAuth: FirebaseAuth
    fAuth = Firebase.auth

    var hasImage by remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val imagePicker =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent(),
            onResult = { uri ->
                hasImage = uri != null
                imageUri = uri
            })

    if(invalidForm){
        Toast.makeText(LocalContext.current, "Please fill all fields of form!", Toast.LENGTH_SHORT).show()
        invalidForm = false
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(top = 5.dp, bottom = 20.dp)
            .verticalScroll(
                rememberScrollState()
            )
    ) {
        ImagePicker(imagePicker, imageUri, hasImage)

        MyOutlinedInputText(
            value = vehicleLicensePlate,
            onValueChange = {
                vehicleLicensePlate = it
            },
            placeholder = stringResource(id = R.string.vehicle_license_plate),
            inputType = KeyboardType.Text,
            icon = Icons.Filled.SortByAlpha,
            enabled = true
        )

        MyOutlinedInputText(
            value = vehicleName,
            onValueChange = {
                vehicleName = it
            },
            placeholder = stringResource(id = R.string.vehicle_name),
            inputType = KeyboardType.Text,
            icon = Icons.Filled.DriveFileRenameOutline,
            enabled = true
        )

        MyOutlinedInputText(
            value = vehicleBrand,
            onValueChange = {
                vehicleBrand = it
            },
            placeholder = stringResource(id = R.string.vehicle_brand),
            inputType = KeyboardType.Text,
            icon = Icons.Filled.DirectionsCar,
            enabled = true
        )

        MyOutlinedInputText(
            value = vehicleModel,
            onValueChange = {
                vehicleModel = it
            },
            placeholder = stringResource(id = R.string.vehicle_model),
            inputType = KeyboardType.Text,
            icon = Icons.Filled.DirectionsCar,
            enabled = true
        )

        MyOutlinedInputText(
            value = vehicleKms,
            onValueChange = {
                vehicleKms = it
            },
            placeholder = stringResource(id = R.string.vehicle_kms),
            inputType = KeyboardType.Number,
            icon = Icons.Filled.Speed,
            enabled = true
        )

        val placeholderUrl =
            "https://d2dgtayfmpkokn.cloudfront.net/wp-content/uploads/sites/614/2021/03/11141058/2020_10_27_JLRNA_LandRover_Inventory_Placeholder_Image-1.jpg"

        MyButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = 10.dp),
            label = stringResource(id = R.string.insert_vehicle),
            contentPadding = PaddingValues(vertical = 12.dp),
        ) {
            if (vehicleLicensePlate != "" && vehicleBrand != "" && vehicleModel != "" && vehicleName != "" && vehicleKms != ""){
                vehiclesViewModels.insertVehicle(
                    Vehicle(
                        vehicleLicensePlate,
                        fAuth.currentUser?.email!!,
                        vehicleBrand,
                        vehicleModel,
                        vehicleName,
                        vehicleKms.toDouble(),
                        "",
                    ),
                    imageUri
                )
                onNavigateBack()
            } else {
                invalidForm = true
            }
        }
    }
}

@Preview
@Composable
fun InsertVehiclesPreview() {
    TravelAssistantTheme {
        InsertVehicles(onNavigateBack = { }, onComposing = {})
    }
}