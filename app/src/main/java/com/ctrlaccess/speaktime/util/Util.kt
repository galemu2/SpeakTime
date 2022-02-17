package com.ctrlaccess.speaktime.util

import java.text.SimpleDateFormat
import java.util.*


var timeFormatter = SimpleDateFormat("h:mm a", Locale.getDefault())
var dateFormatter = SimpleDateFormat("EEEE LLLL d", Locale.getDefault())

/// pattern : "EEE LLL dd, yyyy"
fun convertToTime(time: Long): String {
    return timeFormatter.format(time)
}

fun convertToDate(time: Long): String {
    return dateFormatter.format(time)
}

fun convertToDateAndTime(time: Long): String {
    return ">> Date: ${convertToDate(time = time)} |>> Time: ${convertToTime(time = time)}"
}