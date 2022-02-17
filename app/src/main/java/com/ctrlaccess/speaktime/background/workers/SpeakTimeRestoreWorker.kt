package com.ctrlaccess.speaktime.background.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ctrlaccess.speaktime.background.SpeakTimeService
import com.ctrlaccess.speaktime.data.repositories.SpeakTimeRepository
import com.ctrlaccess.speaktime.util.Const.TAG
import com.ctrlaccess.speaktime.util.Const.WORKER_START_TIME
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import java.util.*
import javax.inject.Inject

@HiltWorker
class SpeakTimeRestoreWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
) : CoroutineWorker(context, params) {

    @Inject
    lateinit var repository: SpeakTimeRepository

    override suspend fun doWork(): Result {
        val appContext = applicationContext
        val startTime = inputData.getLong(WORKER_START_TIME, -1L)
        val stopTime = inputData.getLong(WORKER_START_TIME, -1L)
        if (startTime == -1L || stopTime == -1L) {
            return Result.failure()
        }

        val today = Calendar.getInstance()

        return try {
            Log.d(TAG, "SpeakTimeRestoreWorker: started try block")
            val schedule = repository.schedule.first()

            if (startTime > today.timeInMillis) {
                SpeakTimeService.startSpeakTime(appContext, startTime = startTime)
                Result.success()
            } else if (today.timeInMillis in startTime until stopTime) {

                SpeakTimeService.startSpeakTime(appContext, today.timeInMillis)
                Result.success()

            } else {

                val startCalendar = Calendar.getInstance().apply {
                    timeInMillis = startTime
                    set(Calendar.YEAR, today.get(Calendar.YEAR))
                    set(Calendar.MONTH, today.get(Calendar.MONTH))
                    set(Calendar.DAY_OF_YEAR, today.get(Calendar.DAY_OF_YEAR))
                }

                val stopCalendar = Calendar.getInstance().apply {
                    timeInMillis = stopTime
                    set(Calendar.YEAR, today.get(Calendar.YEAR))
                    set(Calendar.MONTH, today.get(Calendar.MONTH))
                    set(Calendar.DAY_OF_YEAR, today.get(Calendar.DAY_OF_YEAR))
                }

                if (startCalendar.timeInMillis > today.timeInMillis) {
                    // just update database and return success

                    schedule.apply {
                        this.startTime = startCalendar
                        this.stopTime = stopCalendar
                    }
                    repository.updateSchedule(schedule = schedule)
                    SpeakTimeService.startSpeakTime(appContext, startCalendar.timeInMillis)
                    return Result.success()

                } else if (today.timeInMillis in startCalendar.timeInMillis until stopCalendar.timeInMillis) {

                    schedule.apply {
                        this.startTime = startCalendar
                        this.stopTime = stopCalendar
                    }
                    repository.updateSchedule(schedule = schedule)

                    SpeakTimeService.startSpeakTime(appContext, today.timeInMillis)
                    return Result.success()
                } else {
                    val newStartCal = Calendar.getInstance().apply {
                        set(Calendar.DAY_OF_YEAR, startCalendar.get(Calendar.DAY_OF_YEAR).plus(1))
                        set(Calendar.HOUR, startCalendar.get(Calendar.HOUR))
                        set(Calendar.MINUTE, startCalendar.get(Calendar.MINUTE))
                        set(Calendar.SECOND, 0)
                        set(Calendar.MILLISECOND, 0)
                    }
                    val newStopCal = Calendar.getInstance().apply {
                        set(Calendar.DAY_OF_YEAR, stopCalendar.get(Calendar.DAY_OF_YEAR).plus(1))
                        set(Calendar.HOUR, stopCalendar.get(Calendar.HOUR))
                        set(Calendar.MINUTE, stopCalendar.get(Calendar.MINUTE))
                        set(Calendar.SECOND, 0)
                        set(Calendar.MILLISECOND, 0)
                    }

                    schedule.apply {
                        this.startTime = newStartCal
                        this.stopTime = newStopCal
                    }
                    repository.updateSchedule(schedule = schedule)

                    SpeakTimeService.startSpeakTime(appContext, newStartCal.timeInMillis)
                    return Result.success()
                }


            }
        } catch (e: Throwable) {
            Log.d(TAG, "SpeakTimeRestoreWorker: ${e.message}")
            Result.failure()
        }

    }
}