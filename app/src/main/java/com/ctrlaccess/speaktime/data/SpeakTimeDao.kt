package com.ctrlaccess.speaktime.data

import androidx.room.Dao
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ctrlaccess.speaktime.data.models.SpeakTimeSchedule
import kotlinx.coroutines.flow.Flow

@Dao
interface SpeakTimeDao {


    @Query("SELECT * FROM schedule")
    fun getSchedule(): Flow<SpeakTimeSchedule>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateSchedule(schedule: SpeakTimeSchedule)

}