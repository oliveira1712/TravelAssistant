@file:OptIn(ExperimentalMaterialApi::class)

package com.example.travelassistant.ui.screens.more.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.travelassistant.R
import com.example.travelassistant.ui.shared.components.MyButton
import com.example.travelassistant.ui.shared.components.MyRadioGroup
import com.example.travelassistant.ui.theme.TravelAssistantTheme
import com.example.travelassistant.viewmodels.TravelAssistantViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun SettingItem(icon: ImageVector, mainText: String, subText: String, onClick: () -> Unit) {
    Card(
        onClick = { onClick() },
        backgroundColor = MaterialTheme.colors.background,
        modifier = Modifier
            .padding(bottom = 8.dp)
            .fillMaxWidth()
            .shadow(30.dp),
        elevation = 0.dp,
    ) {
        Row(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.clip(shape = RoundedCornerShape(percent = 50))
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .background(MaterialTheme.colors.primary)
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = "",
                            tint = Color.White,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }


                Spacer(modifier = Modifier.width(14.dp))
                Column(
                    modifier = Modifier.offset(y = (2).dp)
                ) {
                    Text(
                        text = mainText,
                        color = Color.Gray,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                    )

                    if (subText != "") {
                        Text(
                            text = subText,
                            color = Color.Gray,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.offset(y = (-4).dp)
                        )
                    }
                }
            }
            Icon(
                imageVector = Icons.Filled.ArrowForwardIos,
                contentDescription = "",
                modifier = Modifier.size(17.dp)
            )

        }
    }
}

@Preview
@Composable
fun SettingItemPreview() {
    TravelAssistantTheme {
        SettingItem(icon = Icons.Default.CardTravel, mainText = "Teste", subText = "", onClick = {})
    }
}

@Composable
fun SettingsSectionTitle(title: String) {
    Text(
        text = title,
        color = MaterialTheme.colors.primary,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Preview
@Composable
fun SettingsSectionTitlePreview() {
    TravelAssistantTheme {
        SettingsSectionTitle(title = "")
    }
}

@Composable
fun ProfileCardUI(onNavigateToUserProfile: () -> Unit) {
    val fAuth: FirebaseAuth
    fAuth = Firebase.auth

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(10.dp)
            .shadow(30.dp),
        backgroundColor = MaterialTheme.colors.background,
        elevation = 0.dp,
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column() {
                Text(
                    text = stringResource(id = R.string.profile_card_title),
                    color = MaterialTheme.colors.primary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                )

                fAuth.currentUser?.email?.let {
                    Text(
                        text = it,
                        color = Color.Gray,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
            AsyncImage(
                modifier = Modifier
                    .size(72.dp)
                    .clip(shape = CircleShape),
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://upload.wikimedia.org/wikipedia/commons/thumb/5/59/User-avatar.svg/2048px-User-avatar.svg.png")
                    .crossfade(true).build(),
                contentDescription = "",
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Preview
@Composable
fun ProfileCardUIPreview() {
    TravelAssistantTheme {
        ProfileCardUI(onNavigateToUserProfile = {})
    }
}

@Composable
fun GeneralOptionsUI(onOpenThemeDialog: () -> Unit, onOpenNavigationDialog: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(horizontal = 14.dp)
            .padding(top = 10.dp)
    ) {
        SettingsSectionTitle("General")

        SettingItem(
            icon = Icons.Filled.Brightness4,
            mainText = stringResource(id = R.string.theme_main_text),
            subText = stringResource(id = R.string.theme_sub_text),
            onClick = onOpenThemeDialog
        )
        SettingItem(
            icon = Icons.Filled.Navigation,
            mainText = stringResource(id = R.string.navigation_preferences_main_text),
            subText = stringResource(id = R.string.navigation_preferences_sub_text),
            onClick = onOpenNavigationDialog
        )
    }
}

@Preview
@Composable
fun GeneralOptionsUIPreview() {
    TravelAssistantTheme {
        GeneralOptionsUI(onOpenThemeDialog = {}, onOpenNavigationDialog = {})
    }
}

@Composable
fun OtherOptionsUI(
    onNavigateToVehicles: () -> Unit,
    onNavigateToSavedInterests: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToRoutes: () -> Unit,
    onNavigateToTripDetails: () -> Unit,
) {
    val travelAssistantViewModel: TravelAssistantViewModel = viewModel()

    Column(
        modifier = Modifier
            .padding(horizontal = 14.dp)
            .padding(top = 10.dp),
    ) {
        SettingsSectionTitle("Other")


        SettingItem(
            icon = Icons.Filled.DirectionsCar,
            mainText = stringResource(id = R.string.vehicles_main_text),
            subText = stringResource(id = R.string.vehicles_sub_text),
            onClick = onNavigateToVehicles
        )

        SettingItem(
            icon = Icons.Filled.Room,
            mainText = stringResource(id = R.string.poi_main_text),
            subText = stringResource(id = R.string.poi_sub_text),
            onClick = onNavigateToSavedInterests
        )

        SettingItem(
            icon = Icons.Filled.Route,
            mainText = stringResource(id = R.string.routes_main_text),
            subText = stringResource(id = R.string.routes_sub_text),
            onClick = onNavigateToRoutes
        )

        SettingItem(
            icon = Icons.Filled.AltRoute,
            mainText = stringResource(id = R.string.tripDetails_main_text),
            subText = stringResource(id = R.string.tripDetails_sub_text),
            onClick = onNavigateToTripDetails
        )

        SettingItem(icon = Icons.Filled.Logout,
            mainText = stringResource(id = R.string.logout),
            subText = "",
            onClick = {
                travelAssistantViewModel.logoutUser()
                onNavigateToLogin()
            })
    }
}

@Preview
@Composable
fun OtherOptionsUI() {
    TravelAssistantTheme {
        OtherOptionsUI(
            onNavigateToVehicles = {},
            onNavigateToSavedInterests = {},
            onNavigateToLogin = {},
            onNavigateToRoutes = {},
            onNavigateToTripDetails = {},
        )
    }
}

@Composable
fun ThemeCustomDialog(
    theme: String,
    onDismiss: () -> Unit,
    onNegativeClick: () -> Unit,
    onPositiveClick: (String) -> Unit,
) {
    val themeModes = listOf(
        stringResource(id = R.string.theme_mode_dark),
        stringResource(id = R.string.theme_mode_light),
        stringResource(id = R.string.theme_mode_system)
    )

    var selectedTheme by remember {
        mutableStateOf(
            when (theme) {
                "Dark" -> themeModes[0]
                "Light" -> themeModes[1]
                else -> themeModes[2]
            }
        )
    }

    Dialog(onDismissRequest = onDismiss) {

        Card(
            elevation = 8.dp, shape = RoundedCornerShape(12.dp)
        ) {

            Column {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        text = stringResource(id = R.string.dialog_theme_title),
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
                        MyRadioGroup(mItems = themeModes, selected = selectedTheme, setSelected = {
                            selectedTheme = it
                        })
                    }
                    // Buttons
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
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
                                onPositiveClick(
                                    selectedTheme
                                )
                            },
                            label = stringResource(id = R.string.apply_text)
                        )
                    }

                }
            }
        }
    }
}


@Preview
@Composable
fun ThemeCustomDialogUI() {
    TravelAssistantTheme {
        ThemeCustomDialog(
            theme = "",
            onDismiss = {},
            onNegativeClick = {},
            onPositiveClick = {}
        )
    }
}

@Composable
fun NavigationPreferencesDialog(
    navigationType: String,
    onDismiss: () -> Unit,
    onNegativeClick: () -> Unit,
    onPositiveClick: (String) -> Unit,
) {
    val themeModes = listOf(
        stringResource(id = R.string.navigation_mode_travelassistant),
        stringResource(id = R.string.navigation_mode_googlemaps),
    )

    var selectedTheme by remember {
        mutableStateOf(
            when (navigationType) {
                "TravelAssistant" -> themeModes[0]
                "GoogleMaps" -> themeModes[1]
                else -> themeModes[0]
            }
        )
    }

    Dialog(onDismissRequest = onDismiss) {

        Card(
            elevation = 8.dp, shape = RoundedCornerShape(12.dp)
        ) {

            Column {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        text = stringResource(id = R.string.dialog_navigation_title),
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
                        MyRadioGroup(mItems = themeModes, selected = selectedTheme, setSelected = {
                            selectedTheme = it
                        })
                    }
                    // Buttons
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
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
                                onPositiveClick(
                                    selectedTheme
                                )
                            },
                            label = stringResource(id = R.string.apply_text)
                        )
                    }

                }
            }
        }
    }
}


@Preview
@Composable
fun NavigationPreferencesDialogPreview() {
    TravelAssistantTheme {
        NavigationPreferencesDialog(
            navigationType = "",
            onDismiss = {},
            onNegativeClick = {},
            onPositiveClick = {})
    }
}