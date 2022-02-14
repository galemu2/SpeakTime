package com.ctrlaccess.speaktime.background

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.ctrlaccess.speaktime.data.models.SpeakTimeSchedule
import com.ctrlaccess.speaktime.data.repositories.SpeakTimeRepository
import com.ctrlaccess.speaktime.ui.viewModels.SpeakTimeViewModel
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
    lateinit var viewModel: SpeakTimeViewModel

    @Inject
    lateinit var repository: SpeakTimeRepository


    override fun doWork(): Result {
        val dispatcher = Dispatchers.Main
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


        /*val requestState = viewModel.schedule.value


        GlobalScope.launch {
            var idel = 1
            while (requestState is RequestState.Idle) {
                Log.d(TAG, "doWork: RequestState.Idle: $idel")
                delay(300)
                idel++
            }

            var loading = 1
            while (requestState is RequestState.Loading) {
                Log.d(TAG, "doWork: RequestState.Loading: $loading")
                delay(300)
                loading++
            }

            if (requestState is RequestState.Success) {
                val schedule = requestState.data
                val startTime = schedule.startTime
                val stopTime = schedule.stopTime
                val start =
                    convertToDate(startTime.timeInMillis) + " " + convertToTime(startTime.timeInMillis)
                val stop =
                    convertToDate(stopTime.timeInMillis) + " " + convertToTime(stopTime.timeInMillis)
                Log.d(TAG, "doWork: $start")
                Log.d(TAG, "doWork: $stop")
            } else {
                Log.d(TAG, "doWork: not really doing work right now $requestState")
            }
        }.start()

*/

    }


}