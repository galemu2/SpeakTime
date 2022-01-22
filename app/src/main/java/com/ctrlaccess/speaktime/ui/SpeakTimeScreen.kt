package com.ctrlaccess.speaktime.ui

import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import com.ctrlaccess.speaktime.data.models.SpeakTimeSchedule
import com.ctrlaccess.speaktime.ui.viewModels.SpeakTimeViewModel
import com.ctrlaccess.speaktime.util.RequestState


@Composable
fun SpeakTimeScreen(viewModel: SpeakTimeViewModel) {


    Scaffold(
        topBar = { SpeakTimeToolbar() },
        content = {
            SpeakTimeContent(
                viewModel = viewModel,
            )
        }
    )
}

