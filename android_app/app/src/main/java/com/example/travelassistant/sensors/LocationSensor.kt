package com.example.travelassistant.sensors

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import androidx.lifecycle.LiveData
import com.example.travelassistant.models.location.LocationDetails
import com.google.android.gms.location.*

class LocationSensor(context: Context, duration: Long) : LiveData<LocationDetails>() {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    private val locationRequest =
        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, duration * 1000)
            .setMinUpdateIntervalMillis(duration * 1000)
            .build()


    @SuppressLint("MissingPermission")
    override fun onActive() {
        super.onActive()

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location.also {
                setLocationData(it)
            }
        }
        startLocationUpdates()
    }

    @SuppressLint("MissingPermission")
    internal fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(
            locationRequest, locationCallback, Looper.getMainLooper()
        )
    }

    private fun setLocationData(location: Location?) {
        location?.let { location1 ->
            value = LocationDetails(location1.longitude.toString(), location1.latitude.toString())
        }
    }

    override fun onInactive() {
        super.onInactive()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            locationResult ?: return
            locationResult.locations.lastOrNull()?.let { location ->
                setLocationData(location)
            }
        }
    }
}
