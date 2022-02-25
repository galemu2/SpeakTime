package com.ctrlaccess.speaktime.background

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import android.widget.Toast
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.ctrlaccess.speaktime.background.workers.SpeakTimeRestoreWorker
import com.ctrlaccess.speaktime.background.workers.SpeakTimeWorker
import com.ctrlaccess.speaktime.util.Const.ACTION_CANCEL_ALARM
import com.ctrlaccess.speaktime.util.Const.ACTION_TRIGGER_SPEAK_TIME
import com.ctrlaccess.speaktime.util.Const.SPEAK_TIME_WORK_NAME
import com.ctrlaccess.speaktime.util.Const.TAG
import com.ctrlaccess.speaktime.util.Const.WORKER_TAG
import com.ctrlaccess.speaktime.util.convertToDate
import com.ctrlaccess.speaktime.util.convertToTime
import java.util.*

class SpeakTimeBroadcast : BroadcastReceiver(), TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var date: String? = null
    private var time: String? = null


    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action == ACTION_TRIGGER_SPEAK_TIME) {
            val cal = Calendar.getInstance()
            date = convertToDate(cal.timeInMillis)
            time = convertToTime(cal.timeInMillis)

            Toast.makeText(context, "started broadcast", Toast.LENGTH_SHORT).show()
            tts = tts ?: TextToSpeech(context, this).apply {
                language = Locale.getDefault()
                setOnUtteranceProgressListener(object : UtteranceProgressListener() {

                    override fun onStart(p0: String?) {
                    }

                    override fun onDone(p0: String?) {
                        removeTts()
                    }

                    override fun onError(p0: String?) {
                    }

                })
            }
        }


        if ((Intent.ACTION_SCREEN_ON == intent.action)) {

            val cal = Calendar.getInstance()
            date = convertToDate(cal.timeInMillis)
            time = convertToTime(cal.timeInMillis)
            Toast.makeText(context, "$date\n$time", Toast.LENGTH_SHORT).show()

            tts = tts ?: TextToSpeech(context, this).apply {
                language = Locale.getDefault()
            }
        }

        if (ACTION_CANCEL_ALARM == intent.action) {
            val serviceIntent = Intent(context, SpeakTimeService::class.java)
            context.stopService(serviceIntent)
            removeTts()
        }

        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {

            Toast.makeText(context, "SpeakTime: Reboot", Toast.LENGTH_SHORT).show()
            Log.d(TAG, "onReceive: SpeakTime: Reboot")

            val workManager = WorkManager.getInstance(context)

            val work = OneTimeWorkRequestBuilder<SpeakTimeWorker>()
                .addTag(WORKER_TAG).build()

            val restoreWork = OneTimeWorkRequest.Builder(SpeakTimeRestoreWorker::class.java)
                .addTag(WORKER_TAG).build()

            var continuation = workManager.beginUniqueWork(
                SPEAK_TIME_WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                work
            )

            continuation = continuation.then(restoreWork)
            continuation.enqueue()

            workManager.getWorkInfosByTagLiveData(WORKER_TAG).observeForever {
                for (i in it) {
                    Log.d(TAG, "onReceive: ${i.outputData.keyValueMap}")
                    Log.d(TAG, "onReceive: ${i.state}")
                }
            }
        }
    }

    private fun removeTts() {
        tts?.stop()
        tts?.shutdown()
        tts = null
    }

    override fun onInit(status: Int) {

        if (status == TextToSpeech.SUCCESS) {
            tts?.speak("$date \n$time", TextToSpeech.QUEUE_FLUSH, null, date)
        }
    }


}

