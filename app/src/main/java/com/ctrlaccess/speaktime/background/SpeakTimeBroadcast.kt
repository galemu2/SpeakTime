package com.ctrlaccess.speaktime.background

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.ctrlaccess.speaktime.util.Const.ACTION_CANCEL_ALARM
import com.ctrlaccess.speaktime.util.Const.TAG
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
                language = Locale.getDefault()
            }
        }

        if (ACTION_CANCEL_ALARM == intent.action) {
            val serviceIntent = Intent(context, SpeakTimeService::class.java)
            context.stopService(serviceIntent)
            tts?.stop()
            tts?.shutdown()
            tts = null
        }

        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {

            Toast.makeText(context, "SpeakTime: Reboot", Toast.LENGTH_SHORT).show()
            Log.d(TAG, "onReceive: RequestState.Success")

            val wm = WorkManager.getInstance(context)

            wm.enqueue(OneTimeWorkRequest.from(SpeakTimeWorker::class.java))
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts?.speak("$date \n$time", TextToSpeech.QUEUE_FLUSH, null, date)
        }
    }
}