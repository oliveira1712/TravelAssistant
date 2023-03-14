package com.example.travelassistant.ui.screens.map

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Start
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.travelassistant.R
import com.example.travelassistant.models.googledirections.*
import com.example.travelassistant.models.googleplaces.placedetails.PointOfInterestDetailsResult
import com.example.travelassistant.ui.shared.components.MyButton
import com.example.travelassistant.ui.shared.components.MyIconButton
import com.example.travelassistant.ui.theme.Sky500
import com.example.travelassistant.ui.theme.TravelAssistantTheme
import com.example.travelassistant.utils.Constants
import com.example.travelassistant.utils.startNavigation
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.PolyUtil
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch

@Composable
fun DrawMap(navigationType: String) {
    val coroutineScope = rememberCoroutineScope()
    val mContext = LocalContext.current
    var isMapLoaded by remember { mutableStateOf(false) }
    var isNavigationMode by remember { mutableStateOf(false) }

    val mapViewModel: MapViewModel = viewModel(LocalContext.current as ComponentActivity)
    val currentLocation = mapViewModel.getLocationLiveData().observeAsState()
    val currentLat = currentLocation.value?.latitude?.toDouble()
    val currentLon = currentLocation.value?.longitude?.toDouble()

    val direction by mapViewModel.directionAPI.observeAsState()
    var currentDirectionStep = mapViewModel.getStepFromCurrentLocation()
    val directionPolylinePoints = direction?.data?.routes?.get(0)?.polyline?.encodedPolyline ?: ""
    val endLocation = direction?.data?.routes?.get(0)?.legs?.get(0)?.endLocation?.latLng

    var locationUpdatesCounter by remember { mutableStateOf(0) }

    val cameraPositionLocation = LatLng(currentLat ?: 0.0, currentLon ?: 0.0)
    var cameraZoom by remember { mutableStateOf(16f) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(cameraPositionLocation, cameraZoom)
    }


    val jsonData = mContext.resources.openRawResource(
        if (MaterialTheme.colors.isLight) R.raw.normal_mode_maps
        else R.raw.night_mode_maps
    ).bufferedReader().use { it.readText() }

    val mapProperties by remember {
        mutableStateOf(
            MapProperties(
                maxZoomPreference = 20f,
                minZoomPreference = 5f,
                isMyLocationEnabled = true,
                mapStyleOptions = MapStyleOptions(jsonData)
            )
        )
    }
    val mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                mapToolbarEnabled = false, zoomControlsEnabled = false
            )
        )
    }

    var isPointOfInterestSelected by remember { mutableStateOf(false) }
    val selectedPointOfInterest = mapViewModel.pointsOfInterestAPI.observeAsState()


    currentDirectionStep?.let {
        coroutineScope.launch {
            val cameraPosition = CameraPosition.fromLatLngZoom(
                LatLng(
                    it.startLocation.latLng.latitude, it.startLocation.latLng.longitude
                ), cameraZoom
            )
            cameraPositionState.animate(
                CameraUpdateFactory.newCameraPosition(cameraPosition), 1_000
            )
        }
    }


    LaunchedEffect(currentLocation.value) {
        if (currentLocation.value != null) {
            locationUpdatesCounter++
        }

        if (isNavigationMode || locationUpdatesCounter == 1) {
            val cameraPosition = CameraPosition.fromLatLngZoom(cameraPositionLocation, cameraZoom)
            cameraPositionState.animate(
                CameraUpdateFactory.newCameraPosition(cameraPosition), 1_000
            )

            currentDirectionStep = mapViewModel.getStepFromCurrentLocation()
        }

    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(modifier = Modifier.matchParentSize(),
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            uiSettings = mapUiSettings,
            onMapLoaded = {
                if (cameraPositionLocation.latitude != 0.0 && cameraPositionLocation.longitude != 0.0) isMapLoaded =
                    true
            },
            onPOIClick = {
                isPointOfInterestSelected = true
                mapViewModel.getPlaceDetailsFromAPI(it.placeId)
                Log.d("SelectedPOI", selectedPointOfInterest.toString())
            }) {
            if (endLocation != null) {
                Marker(
                    state = MarkerState(
                        position = LatLng(
                            endLocation.latitude, endLocation.longitude
                        )
                    ),
                    title = "Destination",
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)
                )
            }


            if (directionPolylinePoints.isNotEmpty()) {
                Polyline(
                    color = Sky500,
                    geodesic = true,
                    width = 20f,
                    points = PolyUtil.decode(directionPolylinePoints)
                )
            }
        }

        if (!isMapLoaded) {
            AnimatedVisibility(
                modifier = Modifier.matchParentSize(),
                visible = !isMapLoaded,
                enter = EnterTransition.None,
                exit = fadeOut()
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .background(MaterialTheme.colors.background)
                        .wrapContentSize()
                )
            }
        }

        if (isPointOfInterestSelected) {
            DrawSelectedPointOfInterestDialog(place = selectedPointOfInterest.value?.data?.result,
                onDismiss = { isPointOfInterestSelected = false },
                onNegativeClick = { isPointOfInterestSelected = false },
                onPositiveClick = {
                    val routeOptions = DirectionRequestBody(
                        destination = Destination(
                            Location(
                                LatLngDirections(
                                    it.geometry.location.lat, it.geometry.location.lng
                                )
                            )
                        ),
                        routeModifiers = RouteModifiers(avoidHighways = true, avoidTolls = true),
                    )

                    startNavigation(
                        context = mContext,
                        mapViewModel = mapViewModel,
                        routeOptions = routeOptions,
                        navigationType = navigationType,
                        onNavigateToMap = null
                    )

                    isPointOfInterestSelected = false
                })
        }

        currentDirectionStep?.let {
            if (isMapLoaded) {
                DrawTopRouteStepsInformations(it)

                val distanceMeters = direction?.data?.routes?.get(0)?.distanceMeters ?: 0
                val durationSeconds =
                    direction?.data?.routes?.get(0)?.duration?.dropLast(1)?.toInt() ?: 0
                DrawBottomRouteInformations(distanceMeters, durationSeconds, onStartNavigation = {
                    isNavigationMode = true
                    cameraZoom = 18f
                    val cameraPosition =
                        CameraPosition.fromLatLngZoom(cameraPositionLocation, cameraZoom)
                    coroutineScope.launch {
                        cameraPositionState.animate(
                            CameraUpdateFactory.newCameraPosition(
                                cameraPosition
                            ), 1_000
                        )
                    }
                }, onCloseNavigation = {
                    mapViewModel.stopDirectionNavigation()
                    currentDirectionStep = null
                    isNavigationMode = false
                    cameraZoom = 16f
                    coroutineScope.launch {
                        cameraPositionState.animate(CameraUpdateFactory.zoomTo(cameraZoom), 1_000)
                    }
                })
            }
        }
    }
}

@Preview
@Composable
fun DrawMapPreview() {
    TravelAssistantTheme {
        DrawMap(navigationType = "TravelAssistant")
    }
}

@Composable
fun DrawSelectedPointOfInterestDialog(
    place: PointOfInterestDetailsResult?,
    onDismiss: () -> Unit,
    onNegativeClick: () -> Unit,
    onPositiveClick: (PointOfInterestDetailsResult) -> Unit,
) {
    var imageURL = ""
    if (place != null) {
        imageURL = if (place.photos != null) {
            "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=${place.photos[0].photo_reference}" + "&key=${Constants.GOOGLE_API_KEY}"
        } else {
            "https://media.istockphoto.com/id/1147544807/vector/thumbnail-image-vector-graphic.jpg?s=612x612&w=0&k=20&c=rnCKVbdxqkjlcs3xH87-9gocETqpspHFXu5dIGB4wuM="
        }
    }

    var isImageLoading by remember { mutableStateOf(true) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.heightIn(300.dp, 400.dp),
            elevation = 8.dp,
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                Text(
                    text = stringResource(id = R.string.selected_point_interest),
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isImageLoading) {
                            CircularProgressIndicator()
                        }

                        AsyncImage(
                            model = imageURL,
                            contentDescription = "Image of the place",
                            contentScale = ContentScale.Crop,
                            onSuccess = {
                                isImageLoading = false
                            },
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(10.dp)),
                        )
                    }

                    if (place != null && !isImageLoading) {
                        Text(
                            text = place.name,
                            modifier = Modifier.padding(top = 5.dp),
                            maxLines = 2,
                            fontSize = 18.sp,
                            color = MaterialTheme.colors.onBackground
                        )
                    }
                }
                // Buttons
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    MyButton(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        buttonColors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.background,
                            contentColor = MaterialTheme.colors.primary,
                        ),
                        elevation = ButtonDefaults.elevation(defaultElevation = 0.dp),
                        border = BorderStroke(
                            1.dp,
                            if (MaterialTheme.colors.isLight) Color.LightGray else Color.DarkGray
                        ),
                        onClick = onNegativeClick,
                        label = stringResource(id = R.string.cancel_text)
                    )

                    Spacer(modifier = Modifier.size(10.dp))

                    MyButton(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        onClick = {
                            if (place != null) {
                                onPositiveClick(place)
                            }
                        },
                        label = stringResource(id = R.string.go_text)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun DrawSelectedPointOfInterestDialogPreview() {
    TravelAssistantTheme {
        DrawSelectedPointOfInterestDialog(
            place = null,
            onDismiss = {},
            onNegativeClick = {},
            onPositiveClick = {})
    }
}

@Composable
fun DrawTopRouteStepsInformations(step: Step) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .background(MaterialTheme.colors.primary.copy(0.9f), RoundedCornerShape(10.dp))
                .border(
                    border = BorderStroke(1.dp, MaterialTheme.colors.primary.copy(1f)),
                    RoundedCornerShape(10.dp)
                ), contentAlignment = Alignment.Center
        ) {
            Text(
                text = step.navigationInstruction?.instructions ?: "",
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 8.dp),
                maxLines = 3,
                style = TextStyle(
                    fontSize = 24.sp,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}

@Composable
fun DrawBottomRouteInformations(
    distanceMeters: Int,
    durationSeconds: Int,
    onStartNavigation: () -> Unit,
    onCloseNavigation: () -> Unit
) {
    val distanceKm = distanceMeters.div(1000)
    val distanceText = if (distanceKm > 0) "$distanceKm Km" else "$distanceMeters m"

    var durationText = ""
    if (durationSeconds > 0) {
        val durationHours = durationSeconds / 3600
        val durationMinutes = (durationSeconds % 3600) / 60
        if (durationHours > 0) {
            durationText += "${durationHours}h"
        }
        if (durationMinutes > 0) {
            durationText += "${durationMinutes}m"
        }
    }


    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(
                    MaterialTheme.colors.background,
                    RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                )
                .padding(horizontal = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .background(Color.Transparent, CircleShape)
                    .border(BorderStroke(1.dp, MaterialTheme.colors.onBackground), CircleShape)
                    .size(50.dp)
                    .clip(CircleShape)
                    .clickable {
                        onCloseNavigation()
                    }, contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close",
                    tint = MaterialTheme.colors.onBackground,
                    modifier = Modifier.size(30.dp)
                )
            }

            Row {
                Row {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = "Distance",
                        tint = MaterialTheme.colors.onBackground,
                        modifier = Modifier.size(30.dp)
                    )
                    Text(
                        text = distanceText,
                        modifier = Modifier.padding(start = 2.dp),
                        style = TextStyle(
                            fontSize = 24.sp,
                            color = MaterialTheme.colors.onBackground,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center
                        )
                    )
                }

                Spacer(Modifier.width(10.dp))

                Row {
                    Icon(
                        imageVector = Icons.Filled.Schedule,
                        contentDescription = "Duration",
                        tint = MaterialTheme.colors.onBackground,
                        modifier = Modifier.size(30.dp)
                    )
                    Text(
                        text = durationText,
                        modifier = Modifier.padding(start = 2.dp),
                        style = TextStyle(
                            fontSize = 24.sp,
                            color = MaterialTheme.colors.onBackground,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }

            MyIconButton(
                onClick = { onStartNavigation() },
                label = "",
                icon = Icons.Filled.Start,
                shape = RoundedCornerShape(5.dp)
            )
        }
    }
}

@Preview
@Composable
fun DrawBottomRouteInformationsPreview() {
    TravelAssistantTheme {
        DrawBottomRouteInformations(
            distanceMeters = 10000,
            durationSeconds = 3600,
            onStartNavigation = {},
            onCloseNavigation = {})
    }
}
