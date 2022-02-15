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
import com.ctrlaccess.speaktime.R
import com.ctrlaccess.speaktime.ui.MainActivity
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

    private lateinit var speakTimeBroadcastReceiver: SpeakTimeBroadcast
    private lateinit var speakTimeStop: String
    private lateinit var speakTimeStart: String

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
                    0
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

            val intent = Intent(context, SpeakTimeBroadcast::class.java)
            intent.action = Const.ACTION_CANCEL_ALARM
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                0
            )

            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                stopTime,
                pendingIntent
            )


        }


    }

    private lateinit var contentTitle: String
    private lateinit var contentDescription: String

    override fun onCreate() {
        super.onCreate()
        contentTitle = getString(R.string.content_title)
        contentDescription = getString(R.string.content_description)
        speakTimeBroadcastReceiver = SpeakTimeBroadcast()

        speakTimeStop = getString(R.string.speak_time_stop)
        speakTimeStart = getString(R.string.speak_time_start)
        val requestState = viewModel.schedule.value

        if (requestState is RequestState.Success) {
            val time = requestState.data.stopTime
            stopSpeakTime(this, time.timeInMillis)
        }

    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val action = intent?.action?: "No Action"
        Log.d(TAG, "onStartCommand: $action")


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

        val requestState = viewModel.schedule.value
        if (requestState is RequestState.Success) {

            if (requestState.data.enabled) {
                val today = Calendar.getInstance()
                requestState.data.startTime.apply {
                    set(Calendar.YEAR, today.get(Calendar.YEAR))
                    set(Calendar.MONTH, today.get(Calendar.MONTH))
                    set(Calendar.DAY_OF_YEAR, today.get(Calendar.DAY_OF_YEAR).plus(1))

                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                startSpeakTime(this, requestState.data.startTime.timeInMillis)

                requestState.data.stopTime.apply {
                    set(Calendar.YEAR, today.get(Calendar.YEAR))
                    set(Calendar.MONTH, today.get(Calendar.MONTH))
                    set(Calendar.DAY_OF_YEAR, today.get(Calendar.DAY_OF_YEAR).plus(1))

                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }

                viewModel.updateSchedule(schedule = requestState.data)
            }
        }
        unregisterReceiver(speakTimeBroadcastReceiver)
        Toast.makeText(this, speakTimeStop, Toast.LENGTH_SHORT).show()

    }


    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}