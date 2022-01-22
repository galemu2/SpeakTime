package com.ctrlaccess.speaktime.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ctrlaccess.speaktime.R
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

    LaunchedEffect(key1 = requestState) {
        schedule = updateValues(requestState = requestState, viewModel = viewModel)
    }

    var startTimeDialogState by remember { mutableStateOf(false) }
    var stopTimeDialogState by remember { mutableStateOf(false) }

    if (startTimeDialogState) {
        DisplayCustomDialog(
            calendar = schedule.startTime,
            updateCalendar = { calendar ->
                schedule.apply {
                    startTime = calendar
                }
                viewModel.updateSchedule(schedule = schedule)
            },
            dialogState = {
                startTimeDialogState = it
            })
    }
    if (stopTimeDialogState) {
        DisplayCustomDialog(
            calendar = schedule.stopTime,
            updateCalendar = { calendar ->
                schedule.apply {
                    stopTime = calendar
                }
                viewModel.updateSchedule(schedule = schedule)
            },
            dialogState = {
                stopTimeDialogState = it
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
            startTimeCalendar = schedule.startTime,
            stopTimeCalendar = schedule.stopTime,
            displayStartDialogState = {
                startTimeDialogState = it
            },
            displayStopDialogState = {
                stopTimeDialogState = it
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
    displayStartDialogState: (Boolean) -> Unit,
    displayStopDialogState: (Boolean) -> Unit,
    enabled: Boolean,
    updateEnabled: (Boolean) -> Unit
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
                    calendar = startTimeCalendar,
                    displayDialogState = displayStartDialogState
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                )

                StopTime(
                    calendar = stopTimeCalendar,
                    displayDialogState = displayStopDialogState
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