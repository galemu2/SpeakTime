package com.ctrlaccess.speaktime.background

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.ctrlaccess.speaktime.MainActivity
import com.ctrlaccess.speaktime.R
import com.ctrlaccess.speaktime.ui.viewModels.SpeakTimeViewModel
import com.ctrlaccess.speaktime.util.Const
import com.ctrlaccess.speaktime.util.Const.CHANNEL_ID
import com.ctrlaccess.speaktime.util.Const.TAG
import com.ctrlaccess.speaktime.util.RequestState
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class SpeakTimeService : Service() {

    private lateinit var speakTimeBroadcastReceiver: SpeakTimeBroadcastReceiver

    @Inject
    lateinit var viewModel: SpeakTimeViewModel

    companion object {

        fun startSpeakTime(context: Context, startTime: Long) {
            val startIntent = Intent(context, SpeakTimeService::class.java)
            val alarmManager: AlarmManager =
                context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val startPendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                PendingIntent.getForegroundService(
                    context,
                    0,
                    startIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            } else {
                PendingIntent.getService(
                    context,
                    0,
                    startIntent,
                    0
                )
            }

            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                startTime,
                startPendingIntent
            )
        }


        fun stopSpeakTime(context: Context, stopTime: Long) {
            val alarmManager: AlarmManager =
                context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            val intent = Intent(context, SpeakTimeBroadcastReceiver::class.java)
            intent.action = Const.ACTION_CANCEL_ALARM
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                0
            )
            /*
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                } else {
                    PendingIntent.FLAG_UPDATE_CURRENT
            }
            */
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                stopTime,
                pendingIntent
            )
            Log.d("TAG", "SpeakTimeService: stopSpeakTime()")
        }
    }

    private lateinit var contentTitle: String
    private lateinit var contentDescription: String

    override fun onCreate() {
        super.onCreate()
        contentTitle = getString(R.string.content_title)
        contentDescription = getString(R.string.content_description)
        speakTimeBroadcastReceiver = SpeakTimeBroadcastReceiver()

        Toast.makeText(this, "SpeakTimeService: onCreate()", Toast.LENGTH_SHORT).show()
        Log.d("TAG", "SpeakTimeService: onCreate()")
        val schedule = viewModel.schedule.value
        if (schedule is RequestState.Success) {

            val time = schedule.data.stopTime
            stopSpeakTime(this, time.timeInMillis)
        }

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
            .setContentTitle(contentTitle)
            .setContentText(contentDescription)
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

        val schedule = viewModel.schedule.value
        if (schedule is RequestState.Success) {
            if (schedule.data.enabled) {

                val cal = Calendar.getInstance()
                schedule.data.startTime = cal

                schedule.data.startTime.apply {
                    set(Calendar.MINUTE, cal.get(Calendar.MINUTE).plus(2))
                }
                startSpeakTime(this, schedule.data.startTime.timeInMillis)

                schedule.data.stopTime = cal
                schedule.data.stopTime.apply {
                    set(Calendar.MINUTE, cal.get(Calendar.MINUTE).plus(4))
                }
            }
            viewModel.updateSchedule(schedule = schedule.data)
            Log.d(TAG, "SpeakTimeService: onDestroy(): ${schedule.data}")
        }

        unregisterReceiver(speakTimeBroadcastReceiver)
        Toast.makeText(this, "SpeakTimeService: onDestroy()", Toast.LENGTH_SHORT).show()

    }


    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}