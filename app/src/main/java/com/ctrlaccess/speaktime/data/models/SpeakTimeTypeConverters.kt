package com.ctrlaccess.speaktime.data.models

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import java.util.*

@ProvidedTypeConverter
class SpeakTimeTypeConverters {

    @TypeConverter
    fun fromCalender(calendar: Calendar): String {
        return calendar.timeInMillis.toString()

    }

    @TypeConverter
    fun toCalendar(string: String): Calendar {
        return Calendar.getInstance().apply {
            timeInMillis = string.toLong()
        }
    }
}