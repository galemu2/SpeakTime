package com.ctrlaccess.speaktime.ui

import android.annotation.SuppressLint
import android.os.Build
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
import com.ctrlaccess.speaktime.util.Const.HOUR
import java.util.*


@SuppressLint("UnrememberedMutableState")
@Composable
fun DisplayCustomDialog(
    dialogState: (Boolean) -> Unit,
    schedule: SpeakTimeSchedule,
    updateCalendar: (SpeakTimeSchedule) -> Unit,
    tabIndex: Int,
) {

    val now = Calendar.getInstance()

    var startTime by mutableStateOf(schedule.startTime)
    var stopTime by mutableStateOf(schedule.stopTime)

    val context = LocalContext.current
    val timeGap = stringResource(id = R.string.time_gap)

    val startTimeEarly = stringResource(id = R.string.time_start)
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
                // start time is latter than now
                if (startTime.timeInMillis > now.timeInMillis) {

                    // at least 1 hour between start and end time
                    val gap = stopTime.timeInMillis - startTime.timeInMillis
                    if (gap > HOUR) {
                        schedule.apply {
                            this.startTime = startTime
                            this.stopTime = stopTime
                        }
                        updateCalendar(schedule)
                        dialogState(false)
                    } else {
                        Toast.makeText(context, timeGap, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, startTimeEarly, Toast.LENGTH_SHORT).show()
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

@Composable
fun CustomDialog(
    modifier: Modifier = Modifier,
    tabIndex: Int = 0,

    updateScheduleTimes: (Calendar, Calendar) -> Unit,
    schedule: SpeakTimeSchedule
) {

    var startTimeCalendar by remember { mutableStateOf(schedule.copy().startTime) }
    var stopTimeCalendar by remember { mutableStateOf(schedule.copy().stopTime) }

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
                    cal = schedule.startTime,
                ) { startTimeCal ->
                    startTimeCalendar = startTimeCal
                }

            }
            1 -> {
                TimePickerView(
                    modifier = modifier,
                    cal = schedule.stopTime,
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
    cal: Calendar,
    updateCalendar: (Calendar) -> Unit,
) {

    val today = Calendar.getInstance()

    val calendar by remember {
        mutableStateOf(cal.apply {
            set(Calendar.YEAR, today.get(Calendar.YEAR))
            set(Calendar.MONTH, today.get(Calendar.MONTH))
            set(Calendar.DAY_OF_YEAR, today.get(Calendar.DAY_OF_YEAR))
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        })
    }

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
