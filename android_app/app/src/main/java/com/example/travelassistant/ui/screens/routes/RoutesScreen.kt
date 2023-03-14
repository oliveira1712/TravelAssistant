package com.example.travelassistant.ui.screens.routes

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travelassistant.R
import com.example.travelassistant.models.navigation.AppBarState
import com.example.travelassistant.ui.screens.routes.components.RoutesCard
import com.example.travelassistant.ui.theme.TravelAssistantTheme

@Composable
fun RoutesScreen(
    onNavigateToMap: () -> Unit,
    onNavigateBack: () -> Unit,
    onComposing: (AppBarState) -> Unit
) {
    val screenName = stringResource(id = R.string.routes_screen)

    LaunchedEffect(key1 = true) {
        onComposing(
            AppBarState(
                title = screenName,
                navigationIcon = {
                    IconButton(onClick = { onNavigateBack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
            )
        )
    }

    val routesViewModel: RoutesViewModel = viewModel()
    val routes = routesViewModel.allRoutes.observeAsState()

    LazyColumn {
        routes.value?.let {
            items(it) { item ->
                RoutesCard(onNavigateToMap, route = item)
            }
        }
    }
}

@Preview
@Composable
fun RoutesScreenPreview() {
    TravelAssistantTheme {
        RoutesScreen(
            {}, onNavigateBack = {}, onComposing = {})
    }
}