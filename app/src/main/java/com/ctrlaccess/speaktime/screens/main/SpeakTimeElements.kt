package com.ctrlaccess.speaktime.screens.main

import android.content.Intent
import android.content.IntentFilter
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.ctrlaccess.speaktime.R
import com.ctrlaccess.speaktime.background.SpeakTimeBroadcast
import com.ctrlaccess.speaktime.ui.theme.*
import com.ctrlaccess.speaktime.ui.viewModels.SpeakTimeViewModel
import com.ctrlaccess.speaktime.util.Const.ACTION_TRIGGER_SPEAK_TIME
import com.ctrlaccess.speaktime.util.convertToTime
import java.util.*

@Composable
fun EnableSpeakTimeButton(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    enabledDialogState: Boolean,
    updateEnabledDialogState: (Boolean) -> Unit
) {



    val enabledText = stringResource(id = R.string.enable)
    val disabledText = stringResource(id = R.string.disable)

    Button(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp),
        onClick = {
            updateEnabledDialogState(!enabledDialogState)
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
            text = if (enabled) disabledText else enabledText,
            color = Color.White
        )
    }
}

@Composable
fun StartTime(
    modifier: Modifier = Modifier,
    displayDialogState: (Boolean, Int) -> Unit,
    calendar: Calendar,
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


            val startTime = convertToTime(calendar.timeInMillis)

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
        val stopTime = convertToTime(calendar.timeInMillis)
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


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SpeakTimeToolbar(viewModel: SpeakTimeViewModel) {
    val context = LocalContext.current

    var expanded by remember { mutableStateOf(false) }

    val defaultTimeSet = stringResource(id = R.string.default_time)
    TopAppBar(
        navigationIcon = {
            Icon(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .clickable {
                        LocalBroadcastManager
                            .getInstance(context)
                            .registerReceiver(
                                SpeakTimeBroadcast(),
                                IntentFilter(ACTION_TRIGGER_SPEAK_TIME)
                            )

                        val intent = Intent(ACTION_TRIGGER_SPEAK_TIME)
                        LocalBroadcastManager
                            .getInstance(context)
                            .sendBroadcast(intent)

                        LocalBroadcastManager
                            .getInstance(context)
                            .unregisterReceiver(
                                SpeakTimeBroadcast()
                            )

                    },
                painter = painterResource(id = R.drawable.ic_watch),
                contentDescription = stringResource(
                    id = R.string.app_icon
                )
            )
        },
        modifier = Modifier
            .wrapContentSize(),
        title = {
            Text(
                modifier = Modifier
                    .padding(start = 16.dp),
                text = stringResource(id = R.string.app_name),
                color = Color.LightGray,
                fontFamily = font2,
                textAlign = TextAlign.Center
            )
        },
        actions = {
            IconButton(onClick = {
                expanded = true

            }) {
                Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "")
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    },
                    content = {
                        Surface(
                            onClick = {
                                viewModel.updateSchedule(viewModel.initialSchedule)
                                viewModel.getSpeakTimeSchedule()

                                Toast.makeText(context, defaultTimeSet, Toast.LENGTH_SHORT).show()
                                expanded = false
                            },
                            modifier = Modifier
                        ) {
                            Text(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(start = 8.dp, end = 8.dp),
                                text = "set default",
                                style = Typography.subtitle2
                            )
                        }
                    })


            }

        }

    )
}