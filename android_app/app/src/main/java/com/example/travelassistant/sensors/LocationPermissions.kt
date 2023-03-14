package com.example.travelassistant.sensors

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat


@Composable
fun locationPermissions(): Int {
    val permissionGiven = remember {
        mutableStateOf(0)
    }
    val ctx = LocalContext.current
    if (ActivityCompat.checkSelfPermission(
            ctx, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            ctx, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        permissionGiven.value = 2
    }
    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                permissionGiven.value += 1
            }else{
                Log.d("GPS", "GPS Unavailable")
            }
        }
    LaunchedEffect(key1 = "Permission") {
        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        permissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
    }
    return permissionGiven.value
}