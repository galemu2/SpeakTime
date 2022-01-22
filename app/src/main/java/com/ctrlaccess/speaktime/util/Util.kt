package com.ctrlaccess.speaktime

import java.text.SimpleDateFormat
import java.util.*


val timeFormatter = SimpleDateFormat("hh:mm a", Locale.US)

fun convertToTime(time: Long): String {
    return timeFormatter.format(time)
}