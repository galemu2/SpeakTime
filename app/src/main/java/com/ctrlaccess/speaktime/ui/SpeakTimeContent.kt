package com.ctrlaccess.speaktime.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ctrlaccess.speaktime.R
import com.ctrlaccess.speaktime.background.SpeakTimeService
import com.ctrlaccess.speaktime.data.models.SpeakTimeSchedule
import com.ctrlaccess.speaktime.ui.viewModels.SpeakTimeViewModel
import com.ctrlaccess.speaktime.util.Const.TAG
import com.ctrlaccess.speaktime.util.RequestState
import com.ctrlaccess.speaktime.util.convertToDate
import com.ctrlaccess.speaktime.util.convertToDateAndTime
import com.ctrlaccess.speaktime.util.convertToTime
import java.util.*

@Composable
fun SpeakTimeContent(
    viewModel: SpeakTimeViewModel,
) {

    val requestState by viewModel.schedule.collectAsState()

    var schedule by remember { mutableStateOf(viewModel.initialSchedule) }

    val context = LocalContext.current

    LaunchedEffect(key1 = requestState) {
        if (requestState is RequestState.Success) {
            Log.d(TAG, "SpeakTimeContent: updating ..")
            schedule = (requestState as RequestState.Success<SpeakTimeSchedule>).data
            Log.d(TAG, "StartTime: ${convertToDateAndTime(schedule.startTime.timeInMillis)}")
            Log.d(TAG, "StopTime: ${convertToDateAndTime(schedule.stopTime.timeInMillis)}")

        } else if (requestState is RequestState.Error) {
            val message = (requestState as RequestState.Error).error.message ?: "Unknown Error!"
            Toast.makeText(context, "ERROR: $message", Toast.LENGTH_SHORT).show()
            Log.d(TAG, "SpeakTimeContent: RequestState.Error: $message")
        }
    }


    var dialogState by remember { mutableStateOf(false) }
    var tabIndex by remember { mutableStateOf(0) }
    if (dialogState) {

        DisplayCustomDialog(
            tabIndex = tabIndex,
            schedule = schedule.copy(),
            updateCalendar = { newSchedule ->

                schedule.apply {
                    startTime = newSchedule.startTime
                    stopTime = newSchedule.stopTime
                }
                viewModel.updateSchedule(schedule = schedule)

                SpeakTimeService.startSpeakTime(
                    context = context,
                    schedule.startTime.timeInMillis
                )

            },
            dialogState = {
                viewModel.getSpeakTimeSchedule()
                dialogState = it
            })
    }


    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            modifier = Modifier.fillMaxHeight(),
            alpha = 0.2f,
            painter = painterResource(
                id = R.drawable.ic_honeycomb
            ),
            contentScale = ContentScale.Crop,
            contentDescription = stringResource(id = R.string.background_img_honeycomb)
        )
        SpeakTimeItem(
            modifier = Modifier
                .background(color = Color.Transparent),
            schedule = schedule,
            dialogState = { state, idx ->
                dialogState = state
                tabIndex = idx
            },
            enabled = schedule.enabled,
            updateEnabled = { isEnabled ->
                schedule.apply {
                    enabled = isEnabled
                    if (isEnabled) {

                        val today = Calendar.getInstance()
                        val startTime = startTime.timeInMillis
                        val stopTime = stopTime.timeInMillis

                        if (today.timeInMillis < stopTime) {
                            if (today.timeInMillis > startTime) {
                                SpeakTimeService.startSpeakTime(context, today.timeInMillis)
                            } else {
                                SpeakTimeService.startSpeakTime(context, startTime = startTime)
                            }
                        } else {

                            this.startTime.apply {
                                set(Calendar.YEAR, today.get(Calendar.YEAR))
                                set(Calendar.MONTH, today.get(Calendar.MONTH))
                                set(Calendar.DAY_OF_YEAR, today.get(Calendar.DAY_OF_YEAR).plus(1))

                                set(Calendar.SECOND, 0)
                                set(Calendar.MILLISECOND, 0)
                            }

                            this.stopTime.apply {
                                set(Calendar.YEAR, today.get(Calendar.YEAR))
                                set(Calendar.MONTH, today.get(Calendar.MONTH))
                                set(Calendar.DAY_OF_YEAR, today.get(Calendar.DAY_OF_YEAR).plus(1))

                                set(Calendar.SECOND, 0)
                                set(Calendar.MILLISECOND, 0)
                            }
                            SpeakTimeService.startSpeakTime(context, startTime = startTime)

                        }

                    } else {
                        SpeakTimeService.stopSpeakTime(context, Calendar.getInstance().timeInMillis)
                    }
                }
                viewModel.updateSchedule(schedule = schedule)
                viewModel.getSpeakTimeSchedule()
            }
        )
    }
}


@Composable
fun SpeakTimeItem(
    modifier: Modifier = Modifier,
    dialogState: (Boolean, Int) -> Unit,
    enabled: Boolean,
    updateEnabled: (Boolean) -> Unit,
    schedule: SpeakTimeSchedule,
) {

    Column(
        modifier = modifier
            .padding(8.dp)
            .fillMaxSize()
    ) {
        Surface(
            color = Color.Transparent,
            modifier = Modifier
                .background(color = Color.Transparent)
                .fillMaxSize()
                .weight(8f)

        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Transparent),
                verticalArrangement = Arrangement.Center
            ) {
                StartTime(
                    calendar = schedule.startTime,
                    displayDialogState = dialogState
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                )

                StopTime(
                    calendar = schedule.stopTime,
                    displayDialogState = dialogState
                )
            }

        }

        Surface(
            modifier = Modifier
                .wrapContentSize()
                .weight(2f)
        ) {

            CancelSpeakTime(enabled = enabled, updateEnabled = updateEnabled)
        }

    }
}

