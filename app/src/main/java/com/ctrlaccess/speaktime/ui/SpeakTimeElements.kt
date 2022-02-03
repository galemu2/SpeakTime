package com.ctrlaccess.speaktime.ui

import android.annotation.SuppressLint
import android.util.Log
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
import com.ctrlaccess.speaktime.R
import com.ctrlaccess.speaktime.ui.theme.*
import com.ctrlaccess.speaktime.util.Const.TAG
import com.ctrlaccess.speaktime.util.convertToTime
import java.util.*

@SuppressLint("UnrememberedMutableState")
@Composable
fun CancelSpeakTime(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    updateEnabled: (Boolean) -> Unit
) {

    LaunchedEffect(key1 = true){
        Log.d(TAG, "CancelSpeakTime: ")
    }
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
    LaunchedEffect(key1 = true){
        Log.d(TAG, "StartTime: ")
    }

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

    LaunchedEffect(key1 = true){
        Log.d(TAG, "StopTime: ")
    }
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