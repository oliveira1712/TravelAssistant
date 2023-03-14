@file:OptIn(ExperimentalComposeUiApi::class)

package com.example.travelassistant.ui.shared.components

import android.content.Context
import android.location.Geocoder
import android.net.Uri
import android.util.Log
import android.view.KeyEvent.ACTION_DOWN
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.travelassistant.R
import com.example.travelassistant.models.Route
import com.example.travelassistant.ui.screens.routes.RoutesViewModel
import com.example.travelassistant.ui.theme.Green700
import com.example.travelassistant.ui.theme.LightTextColor
import com.example.travelassistant.ui.theme.Red500
import com.example.travelassistant.viewmodels.TravelAssistantViewModel
import com.google.android.gms.tasks.Tasks
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.delay


@Composable
fun MyButton(
    modifier: Modifier = Modifier,
    buttonColors: ButtonColors = ButtonDefaults.buttonColors(
        backgroundColor = MaterialTheme.colors.primary, contentColor = Color.White
    ),
    label: String,
    shape: Shape = RoundedCornerShape(0.dp),
    contentPadding: PaddingValues = PaddingValues(vertical = 0.dp),
    elevation: ButtonElevation = ButtonDefaults.elevation(defaultElevation = 2.dp),
    border: BorderStroke = BorderStroke(0.dp, Color.Transparent),
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick,
        elevation = elevation,
        shape = shape,
        border = border,
        modifier = modifier,
        colors = buttonColors,
        contentPadding = contentPadding
    ) {
        Text(text = label)
    }
}

@Composable
fun MyIconButton(
    modifier: Modifier = Modifier,
    buttonColors: ButtonColors = ButtonDefaults.buttonColors(
        backgroundColor = MaterialTheme.colors.primary, contentColor = Color.White
    ),
    label: String,
    shape: Shape = RoundedCornerShape(0.dp),
    contentPadding: PaddingValues = PaddingValues(),
    elevation: ButtonElevation? = ButtonDefaults.elevation(),
    border: BorderStroke = BorderStroke(0.dp, Color.Transparent),
    icon: ImageVector,
    iconColor: Color = Color.Unspecified,
    spaceBetween: Dp = 5.dp,
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick,
        elevation = elevation,
        shape = shape,
        border = border,
        modifier = modifier,
        colors = buttonColors,
        contentPadding = contentPadding
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = "",
                tint = iconColor,
                modifier = Modifier.size(26.dp)
            )
            Spacer(modifier = Modifier.width(spaceBetween))
            Text(text = label, fontSize = 16.sp)
        }
    }
}

@Composable
fun MyIconButton(
    modifier: Modifier = Modifier,
    buttonColors: ButtonColors = ButtonDefaults.buttonColors(
        backgroundColor = MaterialTheme.colors.primary, contentColor = Color.White
    ),
    label: String,
    shape: Shape = RoundedCornerShape(0.dp),
    contentPadding: PaddingValues = PaddingValues(vertical = 0.dp),
    elevation: ButtonElevation? = ButtonDefaults.elevation(defaultElevation = 2.dp),
    border: BorderStroke = BorderStroke(0.dp, Color.Transparent),
    icon: Int,
    iconColor: Color = Color.Unspecified,
    spaceBetween: Dp = 5.dp,
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick,
        elevation = elevation,
        shape = shape,
        border = border,
        modifier = modifier,
        colors = buttonColors,
        contentPadding = contentPadding
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = "",
                tint = iconColor,
                modifier = Modifier.size(26.dp)
            )
            Spacer(modifier = Modifier.width(spaceBetween))
            Text(text = label, color = MaterialTheme.colors.primary, fontSize = 16.sp)
        }
    }
}

@Composable
fun MyTextButton(label: String, onClick: () -> Unit = {}) {
    TextButton(
        onClick = onClick, contentPadding = PaddingValues(vertical = 0.dp)
    ) {
        Text(
            text = label,
            color = LightTextColor,
            fontSize = 12.sp,
        )
    }
}

@Composable
fun MyPasswordOutlinedInputText(
    value: String, onValueChange: (String) -> Unit, placeholder: String, enabled: Boolean
) {
    var isPasswordOpen by remember { mutableStateOf(false) }

    OutlinedTextField(value = value,
        onValueChange = { textFieldValue ->
            onValueChange(textFieldValue)
        },
        label = {
            Text(text = placeholder, color = MaterialTheme.colors.primary)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(top = 10.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedBorderColor = MaterialTheme.colors.primary,
            focusedBorderColor = MaterialTheme.colors.primary,
            textColor = Color.Black,
            cursorColor = MaterialTheme.colors.primary
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true,
        enabled = enabled,
        visualTransformation = if (isPasswordOpen) PasswordVisualTransformation() else VisualTransformation.None,
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_password),
                contentDescription = "",
                tint = MaterialTheme.colors.primary,
                modifier = Modifier.size(24.dp)
            )
        },
        trailingIcon = {
            IconButton(onClick = { isPasswordOpen = !isPasswordOpen }) {
                if (!isPasswordOpen) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_eye_open),
                        contentDescription = "",
                        tint = MaterialTheme.colors.primary,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_eye_close),
                        contentDescription = "",
                        tint = MaterialTheme.colors.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        })
}

@Composable
fun MyOutlinedInputText(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    inputType: KeyboardType,
    icon: Int,
    enabled: Boolean
) {
    val focusManager = LocalFocusManager.current

    OutlinedTextField(value = value,
        onValueChange = { textFieldValue ->
            onValueChange(textFieldValue)
        },
        label = {
            Text(text = placeholder, color = MaterialTheme.colors.primary)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(top = 10.dp)
            .onPreviewKeyEvent {
                if (it.key == Key.Tab && it.nativeKeyEvent.action == ACTION_DOWN) {
                    focusManager.moveFocus(FocusDirection.Down)
                    true
                } else {
                    false
                }
            },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedBorderColor = MaterialTheme.colors.primary,
            focusedBorderColor = MaterialTheme.colors.primary,
            textColor = MaterialTheme.colors.onBackground,
            cursorColor = MaterialTheme.colors.primary
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = inputType, imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
        singleLine = true,
        enabled = enabled,
        leadingIcon = {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = "",
                tint = MaterialTheme.colors.primary,
                modifier = Modifier.size(24.dp)
            )
        })
}

@Composable
fun MyOutlinedInputText(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    inputType: KeyboardType,
    icon: ImageVector,
    enabled: Boolean
) {
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = value,
        onValueChange = { textFieldValue ->
            onValueChange(textFieldValue)
        },
        label = {
            Text(text = placeholder, color = MaterialTheme.colors.primary)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp)
            .padding(top = 10.dp)
            .onPreviewKeyEvent {
                if (it.key == Key.Tab && it.nativeKeyEvent.action == ACTION_DOWN) {
                    focusManager.moveFocus(FocusDirection.Down)
                    true
                } else {
                    false
                }
            },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedBorderColor = MaterialTheme.colors.primary,
            focusedBorderColor = MaterialTheme.colors.primary,
            textColor = MaterialTheme.colors.onBackground,
            cursorColor = MaterialTheme.colors.primary
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = inputType, imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
        singleLine = true,
        enabled = enabled,
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = "",
                tint = MaterialTheme.colors.primary,
                modifier = Modifier.size(24.dp)
            )
        },
    )
}

@Composable
fun ImagePicker(
    imagePicker: ManagedActivityResultLauncher<String, Uri?>, imageUri: Uri?, hasImage: Boolean
) {
    val placeholderUrl =
        "https://www.willow-car-sales.co.uk/wp-content/uploads/2019/11/placeholder-image-1.jpg"

    Box {
        AsyncImage(
            model = if (hasImage && imageUri != null) imageUri else placeholderUrl,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 20.dp
                )
                .size(300.dp)
                .clickable(onClick = { imagePicker.launch("image/*") }),
            contentDescription = "Selected Image"
        )
    }
}

@Composable
fun MyShimmerBrushBackground(): Brush {
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.6f),
    )

    val transition = rememberInfiniteTransition()
    val translateAnim = transition.animateFloat(
        initialValue = 0f, targetValue = 1000f, animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 800, easing = FastOutSlowInEasing
            ), repeatMode = RepeatMode.Reverse
        )
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim.value, y = translateAnim.value)
    )

    return brush
}

@Composable
fun NoResults(title: String, description: String) {
    Column(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.noresults),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp)
        )

        Text(
            text = title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp, bottom = 10.dp),
            style = TextStyle(
                fontSize = 30.sp,
                color = Color.DarkGray,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        )

        Text(
            text = description,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp, vertical = 10.dp),
            style = TextStyle(
                fontSize = 24.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            )
        )
    }
}

@Composable
fun MyButtonGroup(
    modifier: Modifier = Modifier,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    Row(
        modifier = modifier
            .height(50.dp)
            .border(
                width = 1.dp,
                color = if (MaterialTheme.colors.isLight) Color.LightGray else Color.DarkGray,
                shape = RoundedCornerShape(10.dp)
            )
            .fillMaxWidth()
    ) {
        options.forEach { option ->
            val isSelected = option.lowercase() == selectedOption.lowercase()
            Button(modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
                elevation = null,
                border = if (isSelected) BorderStroke(
                    1.dp, MaterialTheme.colors.primary.copy(0.3f)
                ) else BorderStroke(0.dp, Color.Transparent),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (isSelected) MaterialTheme.colors.primary.copy(
                        0.2f
                    )
                    else MaterialTheme.colors.background
                ),
                onClick = { onOptionSelected(option) }) {
                Text(
                    text = option,
                    color = if (isSelected) MaterialTheme.colors.primary else MaterialTheme.colors.onBackground
                )
            }

        }

    }
}

@Composable
fun MyRadioGroup(
    modifier: Modifier = Modifier,
    mItems: List<String>,
    selected: String,
    setSelected: (selected: String) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.Center
    ) {
        mItems.forEach { item ->
            Row(modifier = Modifier
                .clickable { setSelected(item) }
                .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = selected == item, onClick = {
                    setSelected(item)
                })
                Text(text = item, modifier = Modifier.padding(start = 8.dp))
            }
        }
    }
}


@Composable
fun ConnectivityAlert() {
    val travelAssistantViewModel: TravelAssistantViewModel = viewModel()
    val isNetworkAvailable = travelAssistantViewModel.getNetworkAvailability().observeAsState(true)
    var isAlertVisible by remember {
        mutableStateOf(false)
    }

    if (!isNetworkAvailable.value) {
        isAlertVisible = true
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        AnimatedVisibility(
            visible = isAlertVisible, enter = slideInVertically(
                initialOffsetY = { 60 },
                animationSpec = tween(
                    durationMillis = 350, easing = LinearEasing
                )
            ), exit = slideOutVertically(
                targetOffsetY = { 60 }, animationSpec = tween(
                    durationMillis = 350, easing = LinearEasing
                )
            )
        ) {
            if (!isNetworkAvailable.value) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                        .background(Red500),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = stringResource(id = R.string.conn_estabelished))
                }

            } else {
                LaunchedEffect(key1 = Unit, block = {
                    delay(3000)
                    isAlertVisible = false
                })
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                        .background(Green700),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = stringResource(id = R.string.conn_estabelished))
                }
            }
        }
    }
}

@Composable
fun DialogCreateRoute(
    cornerRadius: Dp = 12.dp,
    titleTextStyle: TextStyle = TextStyle(
        color = Color.Black.copy(alpha = 0.87f),
        fontSize = 20.sp
    ),
    onDismiss: () -> Unit
) {
    val routesViewModel: RoutesViewModel = viewModel()
    var startPoint by remember { mutableStateOf("") }
    var endPoint by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = {
            onDismiss()
        }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(size = cornerRadius)
        ) {

            Column(modifier = Modifier.padding(all = 16.dp)) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(
                        space = 8.dp,
                        alignment = Alignment.Start
                    )
                ) {
                    Icon(
                        modifier = Modifier.size(26.dp),
                        imageVector = Icons.Filled.PushPin,
                        contentDescription = "CarIcon",
                        tint = MaterialTheme.colors.primary
                    )

                    Text(
                        text = "Create custom route",
                        style = titleTextStyle
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp)
                ) {
                    MyOutlinedInputText(
                        value = endPoint,
                        onValueChange = { endPoint = it },
                        placeholder = "Insert the trip end point",
                        inputType = KeyboardType.Text,
                        icon = Icons.Filled.Place,
                        enabled = true
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(
                        space = 10.dp,
                        alignment = Alignment.End
                    )
                ) {
                    MyButton(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        onClick = {
                            routesViewModel.insertRouteFirebase(
                                Route(
                                    "",
                                    endPoint = endPoint,
                                    0.0, 0.0,
                                )
                            )
                            onDismiss()
                        },
                        label = stringResource(id = R.string.apply_text)
                    )

                    MyButton(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        onClick = {
                            onDismiss()
                        },
                        label = stringResource(id = R.string.cancel_text)
                    )
                }
            }
        }
    }
}

fun getLatLongFromAddress(address: String, context: Context): Pair<Double, Double>? {
    val geocoder = Geocoder(context)
    try {
        val addresses = geocoder.getFromLocationName(address, 1)
        if (addresses != null && addresses.isNotEmpty()) {
            val latitude = addresses[0].latitude
            val longitude = addresses[0].longitude
            Log.d("Coordenadas", latitude.toString())
            Log.d("Coordenadas", longitude.toString())
            return Pair(latitude, longitude)
        }
    } catch (e: Exception) {
        Log.d("Geocoder", e.message.toString())
    }
    return null
}

fun uploadImageToFirebase(imageUri: Uri): String? {
    val storageRef = Firebase.storage.reference
    val fileRef = storageRef.child("images/${imageUri.lastPathSegment}")
    val metadata = StorageMetadata.Builder().setContentType("image/jpeg").build()
    val uploadTask = fileRef.putFile(imageUri, metadata)

    try {
        val task = Tasks.await(uploadTask)
        val urlTask = fileRef.downloadUrl
        val url = Tasks.await(urlTask)
        Log.d("Firebase", "Image uploaded successfully")
        return url.toString()
    } catch (e: Exception) {
        Log.d("FirebaseError", e.message.toString())
    }
    return null
}
