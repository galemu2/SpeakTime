package com.ctrlaccess.speaktime.ui

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.widget.TimePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import com.ctrlaccess.speaktime.R
import com.ctrlaccess.speaktime.data.models.SpeakTimeSchedule
import com.ctrlaccess.speaktime.util.Const.TAG
import java.util.*


@Composable
fun DisplayCustomDialog(
    dialogState: (Boolean) -> Unit,
    schedule: SpeakTimeSchedule,
    updateCalendar: (SpeakTimeSchedule) -> Unit,
    tabIndex: Int,
) {

    val scheduleCopy by remember {mutableStateOf(schedule.copy())}
    var startTime by remember { mutableStateOf(scheduleCopy.startTime) }
    var stopTime by remember { mutableStateOf(scheduleCopy.stopTime) }


    LaunchedEffect(key1 = true) {
        Log.d(TAG, "DisplayCustomDialog: ")
    }



    AlertDialog(
        modifier = Modifier.wrapContentSize(),
        onDismissRequest = {
            dialogState(false)
        },
        text = {
            CustomDialog(
                tabIndex = tabIndex,
                startTime = startTime,
                stopTime = stopTime,
                updateScheduleTimes = { startTimeCalendar, stopTimeCalendar ->
                    startTime = startTimeCalendar
                    stopTime = stopTimeCalendar
                }
            )
        },
        confirmButton = {
            Button(onClick = {
                scheduleCopy.apply {
                    this.startTime = startTime
                    this.stopTime = stopTime
                }
                updateCalendar(scheduleCopy)
                dialogState(false)
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
    startTime: Calendar,
    stopTime: Calendar,
    updateScheduleTimes: (Calendar, Calendar) -> Unit
) {

    LaunchedEffect(key1 = true) {
        Log.d(TAG, "CustomDialog: ")
    }
    var startTimeCalendar by mutableStateOf(startTime)
    var stopTimeCalendar by mutableStateOf(stopTime)

    var selectedTabIndex by remember { mutableStateOf(tabIndex) }

    LaunchedEffect(key1 = true) {
        selectedTabIndex = tabIndex
        Log.d(
            TAG, "CustomDialog#" +
                    "\nselectedTabIndex = $selectedTabIndex" +
                    "\ntabIndex = $tabIndex"
        )

    }

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
                    calendar = startTimeCalendar,
                ) { startTimeCal ->
                    startTimeCalendar = startTimeCal
                }

            }
            1 -> {
                TimePickerView(
                    modifier = modifier,
                    calendar = stopTimeCalendar,
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

    LaunchedEffect(key1 = true) {
        Log.d(TAG, "TimePickerView: ")
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
