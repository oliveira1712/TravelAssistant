package com.example.travelassistant.ui.screens.profile

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.example.travelassistant.models.navigation.AppBarState
import com.example.travelassistant.ui.screens.profile.components.ProfileUI
import com.example.travelassistant.ui.theme.TravelAssistantTheme

@Composable
fun ProfileScreen(onNavigateBack: () -> Unit, onComposing: (AppBarState) -> Unit) {
    var editMode by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        onComposing(
            AppBarState(title = "Profile Screen",
                navigationIcon = {
                    IconButton(onClick = { onNavigateBack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack, contentDescription = null
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { editMode = true }) {
                        Icon(
                            imageVector = Icons.Filled.Edit, contentDescription = null
                        )
                    }
                }
            )
        )
    }

    ProfileUI(editMode = editMode, onNavigateBack = onNavigateBack)
}


@Preview
@Composable
fun ProfileScreenPreview() {
    TravelAssistantTheme {
        ProfileScreen(
            onNavigateBack = {},
            onComposing = {})
    }
}