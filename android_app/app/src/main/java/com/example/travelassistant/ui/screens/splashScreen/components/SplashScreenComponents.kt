package com.example.travelassistant.ui.screens.splashScreen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.travelassistant.R
import com.example.travelassistant.ui.screens.tripDetails.TripDetailsScreen
import com.example.travelassistant.ui.theme.TravelAssistantTheme

@Composable
fun Splash() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.app_logo),
            contentDescription = "logo")
    }
}

@Preview
@Composable
fun SplashPreview() {
    TravelAssistantTheme {
        Splash()
    }
}