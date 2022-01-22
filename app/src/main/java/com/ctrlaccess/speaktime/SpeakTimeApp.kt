package com.ctrlaccess.speaktime

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.ctrlaccess.speaktime.util.Const.CHANNEL_ID
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SpeakTimeApp : Application() {

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "speakTimeNotificationChannel"
            val description = "Channel for alarm manager"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(
                CHANNEL_ID,
                name,
                importance
            )

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)

        }
    }
}