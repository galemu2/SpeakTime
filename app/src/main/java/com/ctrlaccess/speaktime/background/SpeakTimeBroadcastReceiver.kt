package com.ctrlaccess.speaktime.background

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.ctrlaccess.speaktime.util.Const.ACTION_CANCEL_ALARM

class SpeakTimeBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        if (Intent.ACTION_SCREEN_ON == intent.action) {
            Toast.makeText(context, "Screen ON", Toast.LENGTH_SHORT).show()
        }

        if (ACTION_CANCEL_ALARM == intent.action) {
            val serviceIntent = Intent(context, SpeakTimeService::class.java)
            context.stopService(serviceIntent)
            Toast.makeText(context, "Cancel Alarm", Toast.LENGTH_SHORT).show()

        }
    }
}