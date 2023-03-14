package com.example.travelassistant.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context

fun createNotificationChannel(channelId: String, context: Context) {
    val name = "TravelAssistantChannel"
    val descriptionText = "My important test channel"
    val importance = NotificationManager.IMPORTANCE_DEFAULT
    val channel = NotificationChannel(channelId, name, importance).apply {
        description = descriptionText
    }

    val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(channel)
}