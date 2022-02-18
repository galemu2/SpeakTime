package com.ctrlaccess.speaktime.util

import java.text.SimpleDateFormat
import java.util.*

const val timeFormat = "h:mm a"
const val timeFormat1 = "h.mm.ss a"
const val dateFormat = "EEEE LLLL d"

var timeFormatter = SimpleDateFormat(timeFormat, Locale.getDefault())
var dateFormatter = SimpleDateFormat(dateFormat, Locale.getDefault())

/// pattern : "EEE LLL dd, yyyy"
fun convertToTime(time: Long): String {
    return timeFormatter.format(time)
}

fun convertToDate(time: Long): String {
    return dateFormatter.format(time)
}

fun convertToDateAndTime(time: Long): String {
    return "|>> Date: ${convertToDate(time = time)} |>> Time: ${convertToTime(time = time)}"
}

fun compareScheduleTime(time1: Long, time2: Long): Boolean {
    return time1 > time2
}