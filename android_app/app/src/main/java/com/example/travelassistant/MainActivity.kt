package com.example.travelassistant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travelassistant.ui.theme.TravelAssistantTheme
import com.example.travelassistant.viewmodels.TravelAssistantViewModel

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val travelAssistantViewModel: TravelAssistantViewModel = viewModel()
            val savedTheme = travelAssistantViewModel.getSavedTheme().collectAsState("")
            val themeMode = when (savedTheme.value) {
                "Light" -> false
                "Dark" -> true
                else -> isSystemInDarkTheme()
            }
            TravelAssistantTheme(darkTheme = themeMode) {
                val windowSize = calculateWindowSizeClass(this)
                TravelAssistant(windowSize.widthSizeClass)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TravelAssistantTheme {
        TravelAssistant(WindowWidthSizeClass.Compact)
    }
}

