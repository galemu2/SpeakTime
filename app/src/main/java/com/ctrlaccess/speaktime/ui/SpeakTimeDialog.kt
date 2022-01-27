package com.ctrlaccess.speaktime.ui

import android.os.Build
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
import java.util.*


@Composable
fun DisplayCustomDialog(
    dialogState: (Boolean) -> Unit,
    updateCalendar: (SpeakTimeSchedule) -> Unit,
    schedule: SpeakTimeSchedule,
    tabIndex: Int
) {


    AlertDialog(
        modifier = Modifier.wrapContentSize(),
        onDismissRequest = {
            dialogState(false)
        },
        text = {
            CustomDialog(
                tabIndex = tabIndex,
                schedule = schedule,
                timePickerUpdateCalendar = updateCalendar
            )
        },
        confirmButton = {
            Button(onClick = {
                updateCalendar(schedule)
                dialogState(false)
            }) {
                Text(text = stringResource(id = R.string.ok))
            }
        },
        dismissButton = {
            Button(onClick = { dialogState(false) }) {
                Text(text = stringResource(id = R.string.cancel))
            }
        }
    )

}

@Composable
fun CustomDialog(
    modifier: Modifier = Modifier,
    timePickerUpdateCalendar: (SpeakTimeSchedule) -> Unit,
    schedule: SpeakTimeSchedule,
    tabIndex: Int = 0
) {

    var startTimeCalendar by remember { mutableStateOf(schedule.startTime) }
    var stopTimeCalendar by remember { mutableStateOf(schedule.stopTime) }

    var selectedTabIndex by remember { mutableStateOf(tabIndex) }
    LaunchedEffect(key1 = tabIndex) {
        selectedTabIndex = tabIndex
        startTimeCalendar = schedule.startTime
        stopTimeCalendar = schedule.stopTime
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
                    schedule.apply {
                        startTime = startTimeCal
                    }
                    timePickerUpdateCalendar(schedule)
                }
            }
            1 -> {
                TimePickerView(
                    modifier = modifier,
                    calendar = stopTimeCalendar,
                ) { stopTimeCal ->
                    schedule.apply {
                        stopTime = stopTimeCal
                    }
                    timePickerUpdateCalendar(schedule)
                }
            }
        }
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
