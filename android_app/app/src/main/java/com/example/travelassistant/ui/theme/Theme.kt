package com.example.travelassistant.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    primary = Orange500,
    primaryVariant = Orange700,
    secondary = Sky500,
    secondaryVariant = Sky700,
)

private val LightColorPalette = lightColors(
    primary = Orange500,
    primaryVariant = Orange700,
    secondary = Sky500,
    secondaryVariant = Sky700,
//    background = Color.White,
//    surface = Color.White,
//    onPrimary = Color.White,
//    onSecondary = Color.Black,
//    onBackground = Color.Black,
//    onSurface = Color.Black
)

@Composable
fun TravelAssistantTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.isStatusBarVisible = false // Status bar
        systemUiController.setStatusBarColor(
            color = colors.background, darkIcons = colors.isLight
        )
    }

    MaterialTheme(
        colors = colors, typography = Typography, shapes = Shapes, content = content
    )
}