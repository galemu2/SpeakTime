package com.ctrlaccess.speaktime.background.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.ctrlaccess.speaktime.data.models.SpeakTimeSchedule
import com.ctrlaccess.speaktime.data.repositories.SpeakTimeRepository
import com.ctrlaccess.speaktime.util.Const.TAG
import com.ctrlaccess.speaktime.util.convertToDate
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.util.*
import javax.inject.Inject

@HiltWorker
class SpeakTimeWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted params: WorkerParameters,
    ) : Worker(context, params) {

    @Inject
    lateinit var repository: SpeakTimeRepository

    override fun doWork(): Result {
        Log.d(TAG, "doWork: Started ...")
        val dispatcher = Dispatchers.IO
        var schedule: SpeakTimeSchedule? = null
        return runBlocking(dispatcher) {
 
            repository.schedule.collect {
                schedule = it
            }

            val today = Calendar.getInstance().timeInMillis
            Log.d(
                TAG,
                "doWork: ${convertToDate(schedule?.stopTime?.timeInMillis ?: today)}"
            )

            if (schedule == null) {
                Log.d(TAG, "doWork: Failure")
                Result.failure()
            }
            else {
                Log.d(TAG, "doWork: Success")
                Result.success()
            }
        }



    }


}