package com.ctrlaccess.speaktime.data.repositories

import com.ctrlaccess.speaktime.data.SpeakTimeDao
import com.ctrlaccess.speaktime.data.models.SpeakTimeSchedule
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class SpeakTimeRepository @Inject constructor(private val speakTimeDao: SpeakTimeDao) {

    val schedule = speakTimeDao.getSchedule()

    suspend fun updateSchedule(schedule: SpeakTimeSchedule){
        speakTimeDao.updateSchedule(schedule = schedule)
    }

}