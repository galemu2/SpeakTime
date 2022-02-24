package com.ctrlaccess.speaktime.util

import com.ctrlaccess.speaktime.util.Const.HOUR_GAP
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


fun updateCalendarToToday(calendar: Calendar, now: Calendar): Calendar {
    return calendar.apply {
        set(Calendar.DAY_OF_YEAR, now.get(Calendar.DAY_OF_YEAR))
        set(Calendar.MONTH, now.get(Calendar.MONTH))
        set(Calendar.YEAR, now.get(Calendar.YEAR))
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
}

fun updateCalendarDay(calendar: Calendar, now: Calendar): Calendar {
    return if (calendar.timeInMillis < now.timeInMillis) {
        calendar.apply {
            set(Calendar.DAY_OF_YEAR, now.get(Calendar.DAY_OF_YEAR).plus(1))
        }
    } else {
        calendar
    }
}

fun incrementCalendar(calendar: Calendar): Calendar {
    return calendar.apply {
        set(Calendar.DAY_OF_YEAR, this.get(Calendar.DAY_OF_YEAR).plus(1))
    }
}

fun compareStartAndStopTime(startTime: Calendar, stopTime: Calendar): Boolean {
    return stopTime.timeInMillis > startTime.timeInMillis
}

fun compareCalendar2(startTime: Calendar, stopTime: Calendar): Boolean {
    return (stopTime.timeInMillis - startTime.timeInMillis) > HOUR_GAP
}