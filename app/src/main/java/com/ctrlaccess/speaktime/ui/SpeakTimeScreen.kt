package com.ctrlaccess.speaktime.ui

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import com.ctrlaccess.speaktime.ui.viewModels.SpeakTimeViewModel


@Composable
fun SpeakTimeScreen(
    viewModel: SpeakTimeViewModel,
) {

    Scaffold(
        topBar = { SpeakTimeToolbar() },
        content = {
            SpeakTimeContent(
                viewModel = viewModel,
            )
        }
    )
}

