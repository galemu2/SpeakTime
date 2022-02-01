package com.ctrlaccess.speaktime.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ctrlaccess.speaktime.util.Const.table
import com.ctrlaccess.speaktime.util.convertToDate
import com.ctrlaccess.speaktime.util.convertToTime
import java.util.*

@Entity(tableName = table)
data class SpeakTimeSchedule(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var startTime: Calendar,
    var stopTime: Calendar,
    var enabled: Boolean = true,
    var fullTime: Boolean = false

) {

    override fun toString(): String {
        return "schedule#" +
                "\nstartTime: ${convertToDate(startTime.timeInMillis)}" +
                "\nstartTime: ${convertToTime(startTime.timeInMillis)}" +
                "\n" +
                "\nstopTime: ${convertToDate(stopTime.timeInMillis)}" +
                "\nstopTime: ${convertToTime(stopTime.timeInMillis)}"
    }



}
