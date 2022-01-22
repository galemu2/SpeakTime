package com.ctrlaccess.speaktime.background

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.ctrlaccess.speaktime.MainActivity
import com.ctrlaccess.speaktime.R
import com.ctrlaccess.speaktime.util.Const.CHANNEL_ID

class SpeakTimeService : Service() {

    private lateinit var speakTimeBroadcastReceiver: SpeakTimeBroadcastReceiver
    private val TAG = "TAG"
    override fun onCreate() {
        super.onCreate()
        speakTimeBroadcastReceiver = SpeakTimeBroadcastReceiver()
        Toast.makeText(this, "SpeakTimeService: onCreate()", Toast.LENGTH_SHORT).show()

    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            0
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Speak Time")
            .setContentText("Time announces when screen wakes")
            .setSmallIcon(R.drawable.ic_watch)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(1, notification)

        val filter = IntentFilter(Intent.ACTION_SCREEN_ON)
        registerReceiver(speakTimeBroadcastReceiver, filter)


        return START_STICKY
    }


    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(speakTimeBroadcastReceiver)
        Toast.makeText(this, "SpeakTimeService: onDestroy()", Toast.LENGTH_SHORT).show()
    }


    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}