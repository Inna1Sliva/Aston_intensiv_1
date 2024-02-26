package com.example.musicplayer

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.app.NotificationManagerCompat

class RunApp: Application() {
    override fun onCreate() {
        super.onCreate()
        val notificationManager = NotificationManagerCompat.from(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel("channel1Id", "channel1Name", NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = "Music Play"
            notificationManager.createNotificationChannel(channel)
        }
    }
}