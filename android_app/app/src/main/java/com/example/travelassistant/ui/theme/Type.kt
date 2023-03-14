package com.example.travelassistant.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.travelassistant.R

val Jost = FontFamily(
    Font(R.font.jost_bold, weight = FontWeight.Bold),
    Font(R.font.jost_regular, weight = FontWeight.Normal),
    Font(R.font.jost_light, weight = FontWeight.Light),
    Font(R.font.jost_medium, weight = FontWeight.Medium),
    Font(R.font.jost_semibold, weight = FontWeight.SemiBold)
)

// Set of Material typography styles to start with
val Typography = Typography(
    defaultFontFamily = Jost,
    /* body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
     Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)