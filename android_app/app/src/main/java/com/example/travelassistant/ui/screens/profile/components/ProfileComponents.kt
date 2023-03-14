package com.example.travelassistant.ui.screens.profile.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.travelassistant.R
import com.example.travelassistant.ui.shared.components.MyButton
import com.example.travelassistant.ui.shared.components.MyOutlinedInputText
import com.example.travelassistant.ui.shared.components.MyPasswordOutlinedInputText
import com.example.travelassistant.ui.theme.Green500
import com.example.travelassistant.ui.theme.TravelAssistantTheme

@Composable
fun ProfileUI(
    editMode: Boolean,
    onNavigateBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(top = 5.dp, bottom = 20.dp)
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AsyncImage(
            modifier = Modifier
                .size(200.dp)
                .clip(shape = CircleShape),
            model = ImageRequest.Builder(LocalContext.current)
                .data("https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8M3x8cmFuZG9tJTIwcGVvcGxlfGVufDB8fDB8fA%3D%3D&w=1000&q=80")
                .crossfade(true).build(),
            contentDescription = "",
            contentScale = ContentScale.Crop
        )

        MyOutlinedInputText(
            value = "John Doe",
            onValueChange = {},
            placeholder = stringResource(id = R.string.user_name),
            inputType = KeyboardType.Text,
            icon = Icons.Filled.Person,
            enabled = editMode
        )

        MyOutlinedInputText(
            value = "johndoe@gmail.com",
            onValueChange = {},
            placeholder = stringResource(id = R.string.email_address),
            inputType = KeyboardType.Text,
            icon = Icons.Filled.Email,
            enabled = editMode
        )

        MyPasswordOutlinedInputText(
            value = "password123",
            onValueChange = {},
            placeholder = stringResource(id = R.string.password),
            enabled = editMode
        )

        if (editMode) {
            MyButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 20.dp, end = 20.dp, top = 20.dp
                    ),
                buttonColors = ButtonDefaults.buttonColors(
                    backgroundColor = Green500, contentColor = Color.White
                ),
                label = stringResource(id = R.string.save_changes),
                contentPadding = PaddingValues(vertical = 14.dp),
                onClick = {
                    onNavigateBack()
                }
            )
        } else {
            MyButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 20.dp, end = 20.dp, top = 20.dp
                    ),
                buttonColors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Red, contentColor = Color.White
                ),
                label = stringResource(id = R.string.delete_account),
                contentPadding = PaddingValues(vertical = 14.dp)
            )
        }
    }
}

@Preview
@Composable
fun ProfileUIPreview() {
    TravelAssistantTheme {
        ProfileUI(
            editMode = false,
            onNavigateBack = {})
    }
}