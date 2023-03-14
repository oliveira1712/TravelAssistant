package com.example.travelassistant.ui.screens.routes.components

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NearMe
import androidx.compose.material.icons.outlined.Place
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travelassistant.R
import com.example.travelassistant.models.Route
import com.example.travelassistant.models.googledirections.*
import com.example.travelassistant.ui.screens.map.MapViewModel
import com.example.travelassistant.ui.screens.register.RegisterScreen
import com.example.travelassistant.ui.shared.components.MyIconButton
import com.example.travelassistant.ui.theme.Green500
import com.example.travelassistant.ui.theme.Green700
import com.example.travelassistant.ui.theme.TravelAssistantTheme
import com.example.travelassistant.utils.startNavigation
import com.example.travelassistant.viewmodels.TravelAssistantViewModel

@Composable
fun pointLine(label: String, info: String) {
    Column() {
        Text(
            modifier = Modifier.padding(),
            textAlign = TextAlign.Left,
            text = label,
            style = TextStyle(
                color = MaterialTheme.colors.onBackground,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )
        )

        Text(
            modifier = Modifier.padding(vertical = 4.dp, horizontal = 4.dp),
            textAlign = TextAlign.Left,
            text = info,
            style = TextStyle(
                color = MaterialTheme.colors.onBackground,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
            )
        )
    }
}

@Preview
@Composable
fun pointLinePreview() {
    TravelAssistantTheme {
        pointLine(label = "", info = "")
    }
}

@Composable
fun RoutesCard(onNavigateToMap: () -> Unit, route: Route) {
    val mapViewModel: MapViewModel = viewModel(LocalContext.current as ComponentActivity)
    val travelAssistantViewModel: TravelAssistantViewModel = viewModel()
    val navigationType = travelAssistantViewModel.getNavigationType().collectAsState("")
    val mContext = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(175.dp)
            .padding(horizontal = 20.dp, vertical = 10.dp),
        elevation = 5.dp,
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically){
            Column(modifier = Modifier.padding(5.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    pointLine(label = "Ponto de chegada: ", route.endPoint)

                    MyIconButton(
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(vertical = 8.dp),
                        buttonColors = ButtonDefaults.buttonColors(
                            backgroundColor = Green500, contentColor = Color.White
                        ),
                        icon = Icons.Outlined.NearMe,
                        iconColor = Color.White,
                        onClick = {
                            val routeOptions = DirectionRequestBody(
                                destination = Destination(
                                    Location(
                                        LatLngDirections(
                                            route.endPointLat, route.endPointLon
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
                        },
                        label = stringResource(id = R.string.go_text)
                    )
                }
            }
        }
    }
}