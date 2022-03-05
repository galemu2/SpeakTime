package com.ctrlaccess.speaktime.ui.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ctrlaccess.speaktime.data.models.SpeakTimeSchedule
import com.ctrlaccess.speaktime.data.repositories.SpeakTimeRepository
import com.ctrlaccess.speaktime.util.Const.TAG
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
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            set(Calendar.AM_PM, Calendar.PM)
        },
        stopTime = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_YEAR, this.get(Calendar.DAY_OF_YEAR).plus(1))
            set(Calendar.HOUR, 6)
            set(Calendar.MINUTE, 0)
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

    fun getSpeakTimeSchedule() {
        _schedule.value = RequestState.Loading
        try {

            viewModelScope.launch {
                repository.schedule.collect {
                    _schedule.value = RequestState.Success(it)
                }
            }

        } catch (e: Exception) {
            _schedule.value = RequestState.Error(e)
            Log.d(TAG, "Error: ${e.message}")
        }

    }

    fun updateSchedule(schedule: SpeakTimeSchedule) {

        viewModelScope.launch(Dispatchers.IO) {
            repository.updateSchedule(schedule = schedule)
        }

    }

}