package com.example.travelassistant.services

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travelassistant.R
import com.example.travelassistant.models.location.LocationDetails
import com.example.travelassistant.repository.SavedLocationsRepository
import com.example.travelassistant.sensors.LocationSensor
import com.example.travelassistant.viewmodels.TravelAssistantViewModel

class LocationService : Service() {
    private lateinit var locationClient: LocationSensor
    private lateinit var notification: NotificationCompat.Builder
    private lateinit var notificationManager: NotificationManager
    private lateinit var observer: Observer<LocationDetails>
    private val savedLocationsRepository: SavedLocationsRepository = SavedLocationsRepository()

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        locationClient = LocationSensor(applicationContext, 3 * 60) // 3 minutes
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        notification = NotificationCompat.Builder(this, "TravelAssistantChannel")
            .setContentTitle("Tracking location...").setContentText("Location: null")
            .setSmallIcon(R.drawable.app_logo).setOngoing(true)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        observer = Observer<LocationDetails> { location ->
            val lat = location.latitude
            val long = location.longitude
            val updatedNotification = notification.setContentText(
                "Location: ($lat, $long)"
            )

            notificationManager.notify(1, updatedNotification.build())

            savedLocationsRepository.addVisitedLocation(location)
        }

        locationClient.observeForever(observer)

        startForeground(1, notification.build())
    }

    private fun stop() {
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        locationClient.removeObserver(observer)
    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }
}