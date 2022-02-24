package com.ctrlaccess.speaktime.ui

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.widget.TimePicker
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import com.ctrlaccess.speaktime.R
import com.ctrlaccess.speaktime.data.models.SpeakTimeSchedule
import com.ctrlaccess.speaktime.util.*
import com.ctrlaccess.speaktime.util.Const.TAG
import java.util.*


@Composable
fun DisplayCustomDialog(
    dialogState: (Boolean) -> Unit,
    schedule: SpeakTimeSchedule,
    updateCalendar: (SpeakTimeSchedule) -> Unit,
    tabIndex: Int,
) {

    val now = Calendar.getInstance()

    var startTime by remember { mutableStateOf(schedule.copy().startTime) }
    var stopTime by remember { mutableStateOf(schedule.copy().stopTime) }

    val context = LocalContext.current
    val timeGap = stringResource(id = R.string.time_gap)

    AlertDialog(
        modifier = Modifier.wrapContentSize(),
        onDismissRequest = {
            dialogState(false)
        },
        text = {
            CustomDialog(
                tabIndex = tabIndex,
                schedule = schedule,
                updateScheduleTimes = { startTimeCalendar, stopTimeCalendar ->
                    startTime = startTimeCalendar
                    stopTime = stopTimeCalendar
                }
            )
        },
        confirmButton = {
            Button(onClick = {

                // 1. update calendars to today
                startTime = updateCalendarToToday(startTime, now)
                stopTime = updateCalendarToToday(stopTime, now)

                // 2. if calendar is lower than now, increment by 1 day
                startTime = updateCalendarDay(startTime, now)
                stopTime = updateCalendarDay(stopTime, now)

                // 3. if stop time is less than stop time, increment stop time by 1 day
                val stopTimeIsGreater = compareStartAndStopTime(startTime, stopTime)

                // 3.1 else increment stopTime by 1 day
                if (!stopTimeIsGreater) {
                    stopTime = incrementCalendar(stopTime)
                }

                // 4. have at least 1 hour gap
                val oneHourGap = compareCalendar2(startTime, stopTime)

                // start time is latter than now
                if (oneHourGap) {
                    Log.d(TAG, "DisplayCustomDialog: StartTime: ${convertToDateAndTime(startTime.timeInMillis)}")
                    Log.d(TAG, "DisplayCustomDialog: StopTime:  ${convertToDateAndTime(stopTime.timeInMillis)}")
                    schedule.apply {
                        this.startTime = startTime
                        this.stopTime = stopTime
                    }
                    updateCalendar(schedule)
                    dialogState(false)
                } else {
                    Toast.makeText(context, timeGap, Toast.LENGTH_SHORT).show()
                }


            }) {
                Text(text = stringResource(id = R.string.ok))
            }
        },
        dismissButton = {
            Button(onClick = {
                dialogState(false)
            }) {
                Text(text = stringResource(id = R.string.cancel))
            }
        }
    )

}

@SuppressLint("UnrememberedMutableState")
@Composable
fun CustomDialog(
    modifier: Modifier = Modifier,
    tabIndex: Int = 0,
    updateScheduleTimes: (Calendar, Calendar) -> Unit,
    schedule: SpeakTimeSchedule
) {

    var startTimeCalendar by mutableStateOf(schedule.copy().startTime)
    var stopTimeCalendar by mutableStateOf(schedule.copy().stopTime)

    var selectedTabIndex by remember { mutableStateOf(tabIndex) }

    val start = stringResource(id = R.string.start_time)
    val end = stringResource(id = R.string.end_time)
    val tabs = listOf(start, end)

    Column(modifier = Modifier) {

        TabRow(
            modifier = Modifier
                .wrapContentSize()
                .background(color = Color.Transparent),
            selectedTabIndex = selectedTabIndex
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = index == selectedTabIndex,
                    onClick = { selectedTabIndex = index },
                    text = { Text(text = title) })
            }
        }

        when (selectedTabIndex) {
            0 -> {
                TimePickerView(
                    modifier = modifier,
                    calendar = schedule.startTime,
                ) { startTimeCal ->
                    startTimeCalendar = startTimeCal
                }

            }
            1 -> {
                TimePickerView(
                    modifier = modifier,
                    calendar = schedule.stopTime,
                ) { stopTimeCal ->
                    stopTimeCalendar = stopTimeCal
                }
            }
        }
        updateScheduleTimes(startTimeCalendar, stopTimeCalendar)
    }

}

@Composable
private fun TimePickerView(
    modifier: Modifier = Modifier,
    calendar: Calendar,
    updateCalendar: (Calendar) -> Unit,
) {

    AndroidView(
        modifier = modifier,
        factory = { context ->
            TimePicker(context).apply {
                setIs24HourView(false)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    hour = calendar.get(Calendar.HOUR_OF_DAY)
                    minute = calendar.get(Calendar.MINUTE)

                } else {
                    currentHour = calendar.get(Calendar.HOUR_OF_DAY)
                    currentMinute = calendar.get(Calendar.MINUTE)

                }
            }
        },
        update = { timePicker ->

            timePicker.setOnTimeChangedListener { _, hour, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
            }
            updateCalendar(calendar)
        }
    )
}
