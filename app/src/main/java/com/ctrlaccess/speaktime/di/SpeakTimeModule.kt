package com.ctrlaccess.speaktime.di

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ctrlaccess.speaktime.data.SpeakTimeDao
import com.ctrlaccess.speaktime.data.SpeakTimeDatabase
import com.ctrlaccess.speaktime.data.models.SpeakTimeTypeConverters
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SpeakTimeModule {

    @Provides
    @Singleton
    fun provideSpeakTimeDatabase(@ApplicationContext context: Context) :SpeakTimeDatabase {
       return Room.databaseBuilder(
            context,
            SpeakTimeDatabase::class.java,
            "speak_time_database"
        ).addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                val startTime = Calendar.getInstance().apply {
                    set(Calendar.HOUR, 9)
                    set(Calendar.MINUTE, 59)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                    set(Calendar.AM_PM, Calendar.PM)

                }
                val stopTime = Calendar.getInstance().apply {
                    set(Calendar.HOUR, 6)
                    set(Calendar.MINUTE, 59)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                    set(Calendar.AM_PM, Calendar.AM)

                }

                val values = ContentValues()
                values.put("id", 0)
                values.put("startTime", startTime.timeInMillis.toString())
                values.put("stopTime", stopTime.timeInMillis.toString())
                values.put("enabled", "true")
                values.put("fullTime", "false")

                val success = db.insert("schedule", SQLiteDatabase.CONFLICT_IGNORE, values)

            }
        })
            .addTypeConverter(SpeakTimeTypeConverters())
            .build()


    }
    @Provides
    @Singleton
    fun provideSpeakTimeDao(database: SpeakTimeDatabase) :SpeakTimeDao{
        return database.speakTimeDao()
    }

}