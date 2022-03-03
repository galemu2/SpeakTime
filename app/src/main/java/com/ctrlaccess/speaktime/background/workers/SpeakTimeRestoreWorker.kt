package com.ctrlaccess.speaktime.background.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ctrlaccess.speaktime.background.SpeakTimeService
import com.ctrlaccess.speaktime.data.repositories.SpeakTimeRepository
import com.ctrlaccess.speaktime.util.Const.WORKER_START_TIME
import com.ctrlaccess.speaktime.util.Const.WORKER_STOP_TIME
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import java.util.*
import javax.inject.Inject

// todo check if enabled to restart alarmManager
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
        val stopTime = inputData.getLong(WORKER_STOP_TIME, -1L)
        if (startTime == -1L || stopTime == -1L) {
            return Result.failure()
        }

        val today = Calendar.getInstance()
        val startTimeCalendar = Calendar.getInstance().apply {
            timeInMillis = startTime
        }

        val stopTimeCalendar = Calendar.getInstance().apply {
            timeInMillis = stopTime
        }
        return try {
            val schedule = repository.schedule.first()

            if (startTime > today.timeInMillis) {
                SpeakTimeService.startSpeakTime(appContext, startTime = startTime)
                Result.success()
            } else if (today.timeInMillis in startTime until stopTime) {

                SpeakTimeService.startSpeakTime(appContext, today.timeInMillis)
                Result.success()

            } else {

                val startCalendar = Calendar.getInstance().apply {
                    set(Calendar.YEAR, today.get(Calendar.YEAR))
                    set(Calendar.MONTH, today.get(Calendar.MONTH))
                    set(Calendar.DAY_OF_YEAR, today.get(Calendar.DAY_OF_YEAR))
                    set(Calendar.HOUR, startTimeCalendar.get(Calendar.HOUR))
                    set(Calendar.MINUTE, startTimeCalendar.get(Calendar.MINUTE))
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }

                val stopCalendar = Calendar.getInstance().apply {
                    set(Calendar.YEAR, today.get(Calendar.YEAR))
                    set(Calendar.MONTH, today.get(Calendar.MONTH))
                    set(Calendar.DAY_OF_YEAR, today.get(Calendar.DAY_OF_YEAR))
                    set(Calendar.HOUR, stopTimeCalendar.get(Calendar.HOUR))
                    set(Calendar.MINUTE, stopTimeCalendar.get(Calendar.MINUTE))
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }

                if ((startCalendar.timeInMillis > today.timeInMillis)
                    or (today.timeInMillis in startCalendar.timeInMillis until stopCalendar.timeInMillis)
                ) {
                    // just update database and return success

                    schedule.apply {
                        this.startTime = startCalendar
                        this.stopTime = stopCalendar
                    }
                    repository.updateSchedule(schedule = schedule)
                    SpeakTimeService.startSpeakTime(appContext, startCalendar.timeInMillis)
                    return Result.success()

                } else {

                    val newStartCal = startCalendar.apply {
                        set(Calendar.DAY_OF_YEAR, this.get(Calendar.DAY_OF_YEAR).plus(1))
                    }
                    val newStopCal = stopCalendar.apply {
                        set(Calendar.DAY_OF_YEAR, this.get(Calendar.DAY_OF_YEAR).plus(1))
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
            Log.e("SpeakTimeRestoreWorker", "SpeakTimeRestoreWorker: ${e.message}")
            Result.failure()
        }

    }
}