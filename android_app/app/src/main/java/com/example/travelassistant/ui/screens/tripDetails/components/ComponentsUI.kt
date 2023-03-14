package com.example.travelassistant.ui.screens.tripDetails.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelassistant.models.VisitedLocation
import com.example.travelassistant.ui.screens.tripDetails.TripDetailsScreen
import com.example.travelassistant.ui.theme.Orange200
import com.example.travelassistant.ui.theme.TravelAssistantTheme
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun TransparentButton(
    label: String,
    onClick: () -> Unit = {},
    icon: ImageVector,
    iconColor: Color = MaterialTheme.colors.onBackground.copy(0.8f)
) {
    TextButton(
        onClick = onClick,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "",
            tint = iconColor,
            modifier = Modifier.size(26.dp)
        )

        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = label, style = TextStyle(
                fontSize = 16.sp,
                color = MaterialTheme.colors.onBackground.copy(0.8f),
                fontWeight = FontWeight.Bold,
            )
        )

    }
}

@Preview
@Composable
fun TransparentButtonPreview() {
    TravelAssistantTheme {
        TransparentButton(label = "", onClick = {}, icon = Icons.Default.Timer)
    }
}

@Composable
fun DateSection() {
    var pickedDate by remember {
        mutableStateOf(LocalDate.now())
    }

    val dateDialogState = rememberMaterialDialogState()

    val formattedDate by remember {
        derivedStateOf {
            DateTimeFormatter.ofPattern("dd MMM yyyy").format(pickedDate)
        }
    }
    Row(
        modifier = Modifier
            .fillMaxHeight(0.12f)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        TransparentButton(label = "",
            icon = Icons.Default.ChevronLeft,
            iconColor = MaterialTheme.colors.primary,
            onClick = { pickedDate = pickedDate.minusDays(1) })

        TransparentButton(label = formattedDate,
            icon = Icons.Default.CalendarMonth,
            onClick = { dateDialogState.show() })

        TransparentButton(label = "",
            icon = Icons.Default.ChevronRight,
            iconColor = MaterialTheme.colors.primary,
            onClick = { pickedDate = pickedDate.plusDays(1) })
    }
    MaterialDialog(dialogState = dateDialogState, buttons = {
        positiveButton(text = "Ok")
        negativeButton(text = "Cancel")
    }) {
        datepicker(
            initialDate = pickedDate,
            title = "Pick a date",
        ) {
            pickedDate = it
        }
    }
}

@Preview
@Composable
fun DateSectionPreview() {
    TravelAssistantTheme {
        DateSection()
    }
}

@Composable
fun TripDetailsCard(visitedLocation: VisitedLocation) {
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.getDefault())
    val c = Calendar.getInstance()
    c.time = dateFormatter.parse(visitedLocation.startTime) as Date
    val startHour = "${c[Calendar.HOUR_OF_DAY]}:${c[Calendar.MINUTE]}"
    var endHour = ""
    if(visitedLocation.endTime != null){
        c.time = dateFormatter.parse(visitedLocation.endTime!!) as Date
        endHour = "${c[Calendar.HOUR_OF_DAY]}:${c[Calendar.MINUTE]}"
    }


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp)
            .height(120.dp)
            .padding(bottom = 16.dp, top = 2.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = 5.dp
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.25f)
                    .fillMaxHeight()
                    .padding(start = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Column(
                    modifier = Modifier.clip(shape = RoundedCornerShape(18.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .background(Orange200),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Place,
                            contentDescription = "",
                            tint = MaterialTheme.colors.primaryVariant,
                        )
                    }
                }
            }
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = visitedLocation.locality, style = TextStyle(
                                fontSize = 18.sp,
                                color = Color.DarkGray,
                                fontWeight = FontWeight.Bold,
                            )
                        )
                        Text(
                            text = visitedLocation.district, style = TextStyle(
                                fontSize = 13.sp,
                                color = Color.LightGray,
                                fontWeight = FontWeight.Medium,
                            )
                        )
                    }

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Outlined.Timer,
                                contentDescription = "",
                                tint = Color.LightGray,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = startHour, style = TextStyle(
                                    fontSize = 15.sp,
                                    color = Color.LightGray,
                                    fontWeight = FontWeight.SemiBold,
                                )
                            )
                        }

                        if(visitedLocation.endTime != null){
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Filled.Timer,
                                    contentDescription = "",
                                    tint = Color.LightGray,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = endHour, style = TextStyle(
                                        fontSize = 15.sp,
                                        color = Color.LightGray,
                                        fontWeight = FontWeight.SemiBold,
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun TripDetailsCardPreview() {
    val visitedLocation = VisitedLocation("Canedo", "Aveiro",
        "14-01-2023 15:00:00", "14-01-2023 15:10:00")
    TravelAssistantTheme {
        TripDetailsCard(visitedLocation)
    }
}
