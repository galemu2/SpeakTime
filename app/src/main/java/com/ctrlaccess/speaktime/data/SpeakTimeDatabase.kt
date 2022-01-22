package com.ctrlaccess.speaktime.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ctrlaccess.speaktime.data.models.SpeakTimeSchedule
import com.ctrlaccess.speaktime.data.models.SpeakTimeTypeConverters

@Database(entities = [SpeakTimeSchedule::class], version = 1)
@TypeConverters(SpeakTimeTypeConverters::class)
abstract class SpeakTimeDatabase : RoomDatabase() {

    abstract fun speakTimeDao(): SpeakTimeDao

}