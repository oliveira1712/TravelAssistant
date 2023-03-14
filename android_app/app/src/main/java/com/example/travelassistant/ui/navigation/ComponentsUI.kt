package com.example.travelassistant.ui.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.travelassistant.models.navigation.AppBarState
import com.example.travelassistant.models.navigation.NavigationItem


@Composable
fun TopBar(appBarState: AppBarState) {
    if (appBarState.navigationIcon == null){
        TopAppBar(title = { Text(text = appBarState.title, fontSize = 18.sp) },
            actions = {
                appBarState.actions?.invoke(this)
            },
            elevation = 0.dp,
            backgroundColor = MaterialTheme.colors.background,
            contentColor = MaterialTheme.colors.primary
        )
    } else{
        TopAppBar(title = { Text(text = appBarState.title, fontSize = 18.sp) },
            navigationIcon = {
                appBarState.navigationIcon.invoke()
            },
            actions = {
                appBarState.actions?.invoke(this)
            },
            elevation = 0.dp,
            backgroundColor = MaterialTheme.colors.background,
            contentColor = MaterialTheme.colors.primary
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//fun TopBarPreview() {
//    TopBar()
//}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        NavigationItem.Map,
        NavigationItem.Interests,
        NavigationItem.Stations,
        NavigationItem.More,
    )
    val borderColor = if (MaterialTheme.colors.isLight) Color.LightGray else Color.DarkGray
    BottomNavigation(backgroundColor = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.primary,
        elevation = 0.dp,
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                val strokeWidth = 1f
                val x = size.width - strokeWidth

                //top line
                drawLine(
                    color = borderColor,
                    start = Offset(0f, 0f), //(0,0) at top-left point of the box
                    end = Offset(x, 0f), //top-right point of the box
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round
                )
            }
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(icon = {
                Icon(
                    if (currentRoute == item.route) item.iconFilled else item.iconOutlined,
                    modifier = Modifier.size(28.dp),
                    contentDescription = stringResource(id = item.stringResourceId)
                )
            },
                label = {
                    Text(
                        text = stringResource(id = item.stringResourceId), style = TextStyle(
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                        )
                    )
                },

                selectedContentColor = MaterialTheme.colors.primary,
                unselectedContentColor = Color.LightGray,//MaterialTheme.colors.primary.copy(0.4f),
                alwaysShowLabel = false,
                enabled = currentRoute != item.route,
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavigationBarPreview() {
    // BottomNavigationBar()
}