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
import androidx.compose.ui.unit.dp
import com.ctrlaccess.speaktime.R
import com.ctrlaccess.speaktime.background.SpeakTimeService
import com.ctrlaccess.speaktime.data.models.SpeakTimeSchedule
import com.ctrlaccess.speaktime.ui.viewModels.SpeakTimeViewModel
import com.ctrlaccess.speaktime.util.RequestState
import java.util.*

@Composable
fun SpeakTimeContent(
    viewModel: SpeakTimeViewModel
) {

    val requestState by viewModel.schedule.collectAsState()
    var schedule by remember { mutableStateOf(viewModel.initialSchedule) }

    val context = LocalContext.current

    LaunchedEffect(key1 = requestState) {
        schedule = updateValues(requestState = requestState, viewModel = viewModel)

        if (requestState is RequestState.Success) {

            if (schedule.enabled) {
                Log.d("TAG", "is Enabled; setting up start and stop time")

            }

        } else if (requestState is RequestState.Error) {
            val message = (requestState as RequestState.Error).error.message ?: "Unknown Error!"
            Toast.makeText(context, "ERROR: $message", Toast.LENGTH_SHORT).show()
            Log.d("TAG", "SpeakTimeContent: RequestState.Error: $message")
        }
    }


    var dialogState by remember { mutableStateOf(false) }
    var tabIndex by remember { mutableStateOf(0) }
    if (dialogState) {
        DisplayCustomDialog(
            tabIndex = tabIndex,

            schedule = schedule,
            updateCalendar = {
                schedule = it
                viewModel.updateSchedule(schedule = schedule)
                if (tabIndex == 0) {
                    SpeakTimeService.startSpeakTime(
                        context = context,
                        schedule.startTime.timeInMillis
                    )
                    Log.d("TAG", "SpeakTimeContent#startTime# index: $tabIndex")
                } else {
                    SpeakTimeService.stopSpeakTime(
                        context = context,
                        stopTime = schedule.stopTime.timeInMillis
                    )
                    Log.d("TAG", "SpeakTimeContent#stopTime# index: $tabIndex")
                }
            },
            dialogState = {
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
            contentDescription = ""
        )
        SpeakTimeItem(
            modifier = Modifier
                .background(color = Color.Transparent),
            requestState = requestState,
            startTimeCalendar = schedule.startTime,
            stopTimeCalendar = schedule.stopTime,
            dialogState = { state, idx ->
                dialogState = state
                tabIndex = idx
            },
            enabled = schedule.enabled,
            updateEnabled = { isEnabled ->
                schedule.apply {
                    enabled = isEnabled
                }
                viewModel.updateSchedule(schedule = schedule)
            }
        )
    }
}


@Composable
fun SpeakTimeItem(
    modifier: Modifier = Modifier,
    startTimeCalendar: Calendar,
    stopTimeCalendar: Calendar,
    dialogState: (Boolean, Int) -> Unit,
    enabled: Boolean,
    updateEnabled: (Boolean) -> Unit,
    requestState: RequestState<SpeakTimeSchedule>
) {

    var startTime by remember { mutableStateOf(startTimeCalendar) }
    var stopTime by remember { mutableStateOf(stopTimeCalendar) }
    LaunchedEffect(key1 = requestState) {
        if (requestState is RequestState.Success) {
            startTime = requestState.data.startTime
            stopTime = requestState.data.stopTime
        }
    }
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
                    calendar = startTime,
                    displayDialogState = dialogState
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                )

                StopTime(
                    calendar = stopTime,
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

private fun updateValues(
    requestState: RequestState<SpeakTimeSchedule>,
    viewModel: SpeakTimeViewModel
): SpeakTimeSchedule {
    return if (requestState is RequestState.Success) {
        requestState.data
    } else if (requestState is RequestState.Error) {
        viewModel.initialSchedule
    } else {
        viewModel.initialSchedule
    }
}