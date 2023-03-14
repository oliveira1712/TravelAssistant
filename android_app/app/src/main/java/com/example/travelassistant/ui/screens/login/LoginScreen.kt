package com.example.travelassistant.ui.screens.login

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.travelassistant.R
import com.example.travelassistant.models.location.LocationDetails
import com.example.travelassistant.ui.screens.interests.components.DrawSwipableCards
import com.example.travelassistant.ui.shared.components.*
import com.example.travelassistant.ui.theme.BottomBoxShape
import com.example.travelassistant.ui.theme.TravelAssistantTheme
import com.example.travelassistant.viewmodels.TravelAssistantViewModel

@SuppressLint("RememberReturnType")
@Composable
fun LoginScreen(
    travelAssistantViewModel: TravelAssistantViewModel,
    onNavigateToMap: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val viewModel: LoginViewModel = viewModel()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var invalidLoggin by remember { mutableStateOf(false) }
    val loginStatus = viewModel.authState.observeAsState()

    LaunchedEffect(loginStatus.value) {
        if (loginStatus.value == LoginViewModel.AuthStatus.LOGGED){
            travelAssistantViewModel.logInUser()
            onNavigateToMap()
        } else if(loginStatus.value == LoginViewModel.AuthStatus.INCORRECT){
            invalidLoggin = true
        }
    }

    if(invalidLoggin){
        Toast.makeText(LocalContext.current, "Invalid credentials!", Toast.LENGTH_SHORT).show()
        invalidLoggin = false
    }

    // A surface container using the 'background' color from the theme
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(contentAlignment = Alignment.TopCenter) {
            Image(
                painter = painterResource(id = R.drawable.login_illustration),
                contentDescription = "",
                modifier = Modifier.fillMaxWidth()
            )
        }
        Box(contentAlignment = Alignment.BottomCenter) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    backgroundColor = Color.White,
                    elevation = 0.dp,
                    shape = BottomBoxShape.medium
                ) {

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.background(MaterialTheme.colors.background)
                    ) {
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
                            value = password, onValueChange = {
                                password = it
                            }, placeholder = stringResource(id = R.string.password), enabled = true
                        )

                        MyButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = 20.dp, end = 20.dp, top = 20.dp
                                ),
                            label = stringResource(id = R.string.login),
                            contentPadding = PaddingValues(vertical = 14.dp),
                            onClick = {
                                viewModel.login(email, password)
                            },
                        )

                        MyTextButton(label = stringResource(id = R.string.no_account),
                            onClick = { onNavigateToRegister() })
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun LoginCardPreview() {
    val navController = rememberNavController()
    val travelAssistantViewModel: TravelAssistantViewModel = viewModel()

    TravelAssistantTheme {
        LoginScreen(
            travelAssistantViewModel = travelAssistantViewModel,
            onNavigateToMap = { navController.navigate("map") { popUpTo(0) } },
            onNavigateToRegister = { navController.navigate("register") { popUpTo(0) } }
        )
    }
}