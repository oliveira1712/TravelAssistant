package com.example.travelassistant.ui.screens.register

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travelassistant.R
import com.example.travelassistant.ui.shared.components.MyButton
import com.example.travelassistant.ui.shared.components.MyIconButton
import com.example.travelassistant.ui.shared.components.MyOutlinedInputText
import com.example.travelassistant.ui.shared.components.MyPasswordOutlinedInputText
import com.example.travelassistant.ui.theme.SecondaryColor
import com.example.travelassistant.ui.theme.TravelAssistantTheme
import com.example.travelassistant.viewmodels.TravelAssistantViewModel

@Composable
fun RegisterScreen(
    travelAssistantViewModel: TravelAssistantViewModel,
    onNavigateToMap: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val viewModel: RegisterViewModel = viewModel()
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var invalidRegist by remember { mutableStateOf(false) }

    if(invalidRegist){
        Toast.makeText(LocalContext.current, "Please fill all fields of form!", Toast.LENGTH_SHORT).show()
        invalidRegist = false
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(top = 20.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.regist_image),
            contentDescription = "",
            modifier = Modifier.size(160.dp)
        )

        Text(
            text = stringResource(id = R.string.create_your_account),
            textAlign = TextAlign.Center,
            color = SecondaryColor,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 14.dp),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 16.sp,

            )

        MyOutlinedInputText(
            value = username,
            onValueChange = {
                username = it
            },
            placeholder = stringResource(id = R.string.user_name),
            inputType = KeyboardType.Text,
            icon = R.drawable.ic_user,
            enabled = true
        )

        MyOutlinedInputText(
            value = email,
            onValueChange = {
                email = it
            },
            placeholder = stringResource(id = R.string.email_address),
            inputType = KeyboardType.Email,
            icon = R.drawable.ic_email,
            enabled = true
        )

        MyPasswordOutlinedInputText(
            value = password,
            onValueChange = {
                password = it
            },
            placeholder = stringResource(id = R.string.password),
            enabled = true
        )

        MyButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 20.dp, end = 20.dp, top = 20.dp
                ),
            label = stringResource(id = R.string.sign_in),
            contentPadding = PaddingValues(vertical = 14.dp)
        ) {
            if (username != "" && email != "" && password != ""){
                viewModel.register(username, email, password)
                travelAssistantViewModel.logInUser()
                onNavigateToMap()
            } else {

            }
        }

        TextButton(
            onClick = { onNavigateToLogin() }, modifier = Modifier
                .fillMaxWidth()
                .padding()
        ) {
            Text(
                text = stringResource(id = R.string.account_existant),
                color = SecondaryColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}


@Preview
@Composable
fun RegisterScreenPreview() {
    val travelAssistantViewModel: TravelAssistantViewModel = viewModel()
    TravelAssistantTheme {
        RegisterScreen(
            travelAssistantViewModel = travelAssistantViewModel,
            onNavigateToMap = {},
            onNavigateToLogin = {})
    }
}