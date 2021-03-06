package com.ctrlaccess.speaktime.background.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.ctrlaccess.speaktime.data.repositories.SpeakTimeRepository
import com.ctrlaccess.speaktime.util.Const.WORKER_ENABLED
import com.ctrlaccess.speaktime.util.Const.WORKER_START_TIME
import com.ctrlaccess.speaktime.util.Const.WORKER_STOP_TIME
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltWorker
class SpeakTimeWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted params: WorkerParameters,
) : CoroutineWorker(context, params) {

    @Inject
    lateinit var repository: SpeakTimeRepository

    override suspend fun doWork(): Result {

        return try {

            val schedule = repository.schedule.first()

            val startTime = schedule.startTime.timeInMillis
            val stopTime = schedule.stopTime.timeInMillis
            val enabled = schedule.enabled

            val outputData = workDataOf(
                WORKER_STOP_TIME to stopTime,
                WORKER_START_TIME to startTime,
                WORKER_ENABLED to enabled
            )

            Result.success(outputData)

        } catch (e: Throwable) {

            Result.failure()
        }

    }


}