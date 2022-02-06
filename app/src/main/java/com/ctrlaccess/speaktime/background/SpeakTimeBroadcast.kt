package com.ctrlaccess.speaktime.background

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.speech.tts.TextToSpeech
import android.widget.Toast
import com.ctrlaccess.speaktime.util.Const.ACTION_CANCEL_ALARM
import com.ctrlaccess.speaktime.util.convertToDate
import com.ctrlaccess.speaktime.util.convertToTime
import java.util.*

class SpeakTimeBroadcast : BroadcastReceiver(), TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var date = convertToDate(Calendar.getInstance().timeInMillis)
    private var time = convertToTime(Calendar.getInstance().timeInMillis)

    override fun onReceive(context: Context, intent: Intent) {

        if (Intent.ACTION_SCREEN_ON == intent.action) {

            val cal = Calendar.getInstance()
            date = convertToDate(cal.timeInMillis)
            time = convertToTime(cal.timeInMillis)
            Toast.makeText(context, "$date\n$time", Toast.LENGTH_SHORT).show()

            tts = TextToSpeech(context, this).apply {
                language = Locale.US
            }


        }

        if (ACTION_CANCEL_ALARM == intent.action) {
            val serviceIntent = Intent(context, SpeakTimeService::class.java)
            context.stopService(serviceIntent)

        }

        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
            val today = Calendar.getInstance()
            // context.startForegroundService() todo need to reschedule speakTime
            Toast.makeText(context, "SpeakTime:boot completed", Toast.LENGTH_SHORT).show()

        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts?.speak("$date \n$time", TextToSpeech.QUEUE_FLUSH, null, date)

        }
    }


}