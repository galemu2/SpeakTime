package com.ctrlaccess.speaktime.background

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.ctrlaccess.speaktime.util.Const.ACTION_CANCEL_ALARM
import com.ctrlaccess.speaktime.util.convertToDate
import com.ctrlaccess.speaktime.util.convertToTime
import java.util.*

class SpeakTimeBroadcast : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        if (Intent.ACTION_SCREEN_ON == intent.action) {

            val cal = Calendar.getInstance()
            val date = convertToDate(cal.timeInMillis)
            val time = convertToTime(cal.timeInMillis)
            Toast.makeText(context, "$date\n$time", Toast.LENGTH_SHORT).show()
        }

        if (ACTION_CANCEL_ALARM == intent.action) {
            val serviceIntent = Intent(context, SpeakTimeService::class.java)
            context.stopService(serviceIntent)

        }
    }


}