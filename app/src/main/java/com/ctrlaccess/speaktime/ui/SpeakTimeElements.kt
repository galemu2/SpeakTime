package com.ctrlaccess.speaktime.ui

import android.annotation.SuppressLint
import android.os.Build
import android.widget.TimePicker
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.ctrlaccess.speaktime.R
import com.ctrlaccess.speaktime.data.models.SpeakTimeSchedule
import com.ctrlaccess.speaktime.util.convertToTime
import com.ctrlaccess.speaktime.ui.theme.*
import java.util.*

@SuppressLint("UnrememberedMutableState")
@Composable
fun CancelSpeakTime(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    updateEnabled: (Boolean) -> Unit
) {
    var isChecked by mutableStateOf(enabled)

    val enabledText = stringResource(id = R.string.enable)
    val disabledText = stringResource(id = R.string.disable)
    Button(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp),
        onClick = {
            isChecked = !isChecked
            updateEnabled(isChecked)
        },
        elevation = ButtonDefaults.elevation(
            defaultElevation = 4.dp
        ),
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color5,
        )
    ) {

        Text(
            modifier = Modifier
                .align(CenterVertically)
                .wrapContentSize(),
            text = if (isChecked) disabledText else enabledText,
            color = Color.White
        )
    }
}


@Composable
fun StartTime(
    modifier: Modifier = Modifier,
    displayDialogState: (Boolean, Int) -> Unit,
    calendar: Calendar
) {


    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .border(1.dp, color = borderColor, shape = RoundedCornerShape(16.dp))
            .clip(shape = RoundedCornerShape(16.dp))
            .background(color = backgroundColor)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween

    ) {

        val startTime by mutableStateOf(convertToTime(calendar.timeInMillis))
        Text(
            text = stringResource(id = R.string.start_time),
            modifier = Modifier
                .wrapContentSize()
                .align(alignment = CenterVertically)
                .padding(start = 8.dp),
            fontSize = Typography.h4.fontSize,
            color = textColor1,
            fontWeight = FontWeight.Bold
        )

        OutlinedButton(
            modifier = Modifier
                .border(
                    2.dp,
                    color = outlinedButtonBorder,
                    shape = RoundedCornerShape(8.dp)
                )
                .clip(RoundedCornerShape(8.dp)),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = outlinedButtonBackground,
                contentColor = textColor1
            ),
            onClick = { displayDialogState(true, 0) },

            ) {
            Text(
                text = startTime,
                modifier = Modifier
                    .wrapContentSize(),
                fontSize = Typography.h4.fontSize
            )
        }


    }
}

@Composable
fun StopTime(
    modifier: Modifier = Modifier,
    displayDialogState: (Boolean, Int) -> Unit,
    calendar: Calendar
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .border(1.dp, color = borderColor, shape = RoundedCornerShape(16.dp))
            .clip(shape = RoundedCornerShape(16.dp))
            .background(color = backgroundColor)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween

    ) {

        val stopTime by mutableStateOf(convertToTime(calendar.timeInMillis))
        Text(
            text = stringResource(id = R.string.end_time),
            modifier = Modifier
                .wrapContentSize()
                .align(alignment = CenterVertically)
                .padding(start = 8.dp),
            fontSize = Typography.h4.fontSize,
            color = textColor1,
            fontWeight = FontWeight.Bold
        )

        OutlinedButton(
            modifier = Modifier
                .border(2.dp, color = outlinedButtonBorder, shape = RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp)),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = outlinedButtonBackground,
                contentColor = textColor1
            ),
            onClick = { displayDialogState(true, 1) },

            ) {
            Text(
                text = stopTime,
                modifier = Modifier
                    .wrapContentSize(),
                fontSize = Typography.h4.fontSize
            )
        }

    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun DisplayCustomDialog(
    dialogState: (Boolean) -> Unit,
    updateCalendar: (SpeakTimeSchedule) -> Unit,
    schedule: SpeakTimeSchedule,
    tabIndex: Int
) {


    AlertDialog(
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

    var startTimeCalendar by remember { mutableStateOf(schedule.startTime )}
    var stopTimeCalendar by remember { mutableStateOf(schedule.stopTime )}

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

        TabRow(selectedTabIndex = selectedTabIndex) {
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
                    timePickerUpdateCalendar =  timePickerUpdateCalendar,
                    schedule = schedule,
                    tabIndex = tabIndex
                )
            }
            1 -> {
                TimePickerView(
                    modifier = modifier,
                    calendar = stopTimeCalendar,
                    timePickerUpdateCalendar = timePickerUpdateCalendar,
                    schedule = schedule,
                    tabIndex = tabIndex

                )
            }
        }
    }

}

@SuppressLint("UnrememberedMutableState")
@Composable
private fun TimePickerView(
    modifier: Modifier = Modifier,
    calendar: Calendar,
    timePickerUpdateCalendar: (SpeakTimeSchedule) -> Unit,
    schedule: SpeakTimeSchedule,
    tabIndex: Int,
) {
/*    val context = LocalContext.current
    var cal = if (tabIndex == 0) schedule.startTime else schedule.stopTime
    var calendar by   mutableStateOf(cal)
    LaunchedEffect(key1 = tabIndex) {
        cal = if (tabIndex == 0) schedule.startTime else schedule.stopTime
        calendar = cal
    }*/
    AndroidView(
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
        modifier = modifier,
        update = { timePicker ->

            timePicker.setOnTimeChangedListener { _, hour, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)

            }
            schedule.apply {
                if(tabIndex == 0){
                    startTime = calendar
                }else{
                    stopTime = calendar
                }
            }
            timePickerUpdateCalendar(schedule)
        }
    )
}


@Composable
fun SpeakTimeToolbar() {
    val context = LocalContext.current
    TopAppBar(
        modifier = Modifier
            .wrapContentSize(),
        elevation = 4.dp,
    ) {
        Icon(
            modifier = Modifier
                .padding(start = 8.dp)
                .clickable {
                    Toast
                        .makeText(context, "Icon Clicked", Toast.LENGTH_SHORT)
                        .show()
                },
            painter = painterResource(id = R.drawable.ic_watch),
            contentDescription = stringResource(
                id = R.string.app_icon
            )
        )
        Text(
            modifier = Modifier
                .align(CenterVertically)
                .padding(start = 16.dp),
            text = stringResource(id = R.string.app_name),
            color = Color.LightGray,
            fontFamily = font2,
            textAlign = TextAlign.Center
        )
    }
}