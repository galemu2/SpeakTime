package com.ctrlaccess.speaktime.ui.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ctrlaccess.speaktime.data.models.SpeakTimeSchedule
import com.ctrlaccess.speaktime.data.repositories.SpeakTimeRepository
import com.ctrlaccess.speaktime.util.RequestState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SpeakTimeViewModel @Inject constructor(private val repository: SpeakTimeRepository) :
    ViewModel() {

    private var _schedule = MutableStateFlow<RequestState<SpeakTimeSchedule>>(RequestState.Idle)


      val initialSchedule = SpeakTimeSchedule(
        startTime = Calendar.getInstance().apply {
            set(Calendar.HOUR, 10)
            set(Calendar.MINUTE, 1)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            set(Calendar.AM_PM, Calendar.PM)
        },
        stopTime = Calendar.getInstance().apply {
            set(Calendar.HOUR, 7)
            set(Calendar.MINUTE, 1)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            set(Calendar.AM_PM, Calendar.AM)
        },
    )

    init {
        getSpeakTimeSchedule()
    }

    val schedule: StateFlow<RequestState<SpeakTimeSchedule>>
        get() = _schedule

    private fun getSpeakTimeSchedule() {
        _schedule.value = RequestState.Loading

        try {
            viewModelScope.launch(Dispatchers.IO) {

                repository.schedule.collect {
                    _schedule.value = RequestState.Success(it)
                }
            }
        } catch (e: Exception) {
            _schedule.value = RequestState.Error(e)
            Log.d("TAG", "Error: ${e.message}")
        }

    }

    fun updateSchedule(schedule: SpeakTimeSchedule) {

        viewModelScope.launch(Dispatchers.Main) {
            repository.updateSchedule(schedule = schedule)
        }
    }

    var fakeStartTime: Calendar by mutableStateOf(Calendar.getInstance()).apply {
//        this.value.set(Calendar.MINUTE, this.value.get(Calendar.MINUTE).plus(1))
        this.value.set(Calendar.SECOND, 0)
        this.value.set(Calendar.MILLISECOND, 0)
    }

    var fakeStopTime: Calendar by mutableStateOf(Calendar.getInstance().apply {
//        set(Calendar.HOUR, startTimeCalendar.get(Calendar.HOUR).plus(1))
        set(Calendar.MINUTE, fakeStartTime.get(Calendar.MINUTE).plus(2))
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    })


}