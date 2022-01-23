package com.ctrlaccess.speaktime

import android.app.AlarmManager
import android.app.PendingIntent.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.ctrlaccess.speaktime.background.SpeakTimeBroadcastReceiver
import com.ctrlaccess.speaktime.background.SpeakTimeService
import com.ctrlaccess.speaktime.ui.SpeakTimeScreen
import com.ctrlaccess.speaktime.ui.theme.SpeakTimeTheme
import com.ctrlaccess.speaktime.ui.viewModels.SpeakTimeViewModel
import com.ctrlaccess.speaktime.util.Const.ACTION_CANCEL_ALARM
import com.ctrlaccess.speaktime.util.RequestState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    private val viewModel: SpeakTimeViewModel by viewModels()

    //    private lateinit var pendingIntent: PendingIntent
    private lateinit var alarmManager: AlarmManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpeakTimeTheme {
                SpeakTimeScreen(viewModel = viewModel)
            }
        }
        val value = viewModel.schedule.value
        if (value is RequestState.Success) {
            setupAlarmManager(value.data.startTime.timeInMillis)
            setupCancelingAlarm(value.data.stopTime.timeInMillis)
            Log.d("TAG", "background setup")
        }
    }

    private fun setupCancelingAlarm(stopTime: Long) {
        val intent = Intent(this, SpeakTimeBroadcastReceiver::class.java)
        intent.action = ACTION_CANCEL_ALARM
        val pendingIntent = getBroadcast(
            this,
            0,
            intent,
            0
        )
        val cancelAlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        cancelAlarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            stopTime,
            pendingIntent
        )
    }

    private fun setupAlarmManager(startTime: Long) {
        val intent = Intent(this, SpeakTimeService::class.java)

        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getForegroundService(
                this,
                0,
                intent,
                0
            )
        } else {
            getService(
                this,
                0,
                intent,
                0
            )
        }
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            startTime,
            300_000L,
            pendingIntent
        )


        /*
         alarmManager.setExact(
           AlarmManager.RTC_WAKEUP,
          viewModel.startTimeCalendar.timeInMillis,
           pendingIntent
        )

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            viewModel.startTimeCalendar.timeInMillis,
            "",
            {
                ContextCompat.startForegroundService(this, intent)
            },
            Handler(Looper.getMainLooper())
        )
        */
    }
}
