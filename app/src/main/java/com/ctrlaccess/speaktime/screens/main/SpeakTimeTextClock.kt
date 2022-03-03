package com.ctrlaccess.speaktime.screens.main

import android.view.Gravity
import android.widget.TextClock
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import com.ctrlaccess.speaktime.R
import com.ctrlaccess.speaktime.ui.theme.backgroundColor1
import com.ctrlaccess.speaktime.ui.theme.borderColor
import com.ctrlaccess.speaktime.ui.theme.textClockColor
import com.ctrlaccess.speaktime.util.Const.CLOCK_TEXT_SIZE
import com.ctrlaccess.speaktime.util.timeFormat1

@Composable
fun SpeakTimeTextClock(modifier: Modifier = Modifier) {

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .border(1.dp, color = borderColor, shape = RoundedCornerShape(16.dp))
            .clip(shape = RoundedCornerShape(16.dp))
            .background(color = backgroundColor1)
            .padding(8.dp)
    ) {

        val textColor = textClockColor
        Row {
            AndroidView(
                factory = { context ->
                    TextClock(context).apply {
                        this.format12Hour = timeFormat1
                        this.gravity = Gravity.CENTER
                        this.textSize = CLOCK_TEXT_SIZE
                        this.setTextColor(textColor)
                        typeface = ResourcesCompat.getFont(context, R.font.righteous_regular)

                    }
                },
                modifier = modifier
                    .weight(1f)
                    .padding(end = 4.dp)
                    .wrapContentHeight(align = Alignment.CenterVertically)
                    .background(color = backgroundColor1)


            )
/*
            Text(
                text = "AM",
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 4.dp)
                    .background(Color.Transparent)
                    .wrapContentHeight(align = Alignment.CenterVertically),
                color = textClockColor1,
                fontFamily = font3
            )*/
        }

    }
}


@Preview(showBackground = true)
@Composable
fun SpeakTimeTextClockPreview() {
    SpeakTimeTextClock()
}