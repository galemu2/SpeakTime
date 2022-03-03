package com.ctrlaccess.speaktime.screens.main

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import com.ctrlaccess.speaktime.ui.viewModels.SpeakTimeViewModel


@Composable
fun SpeakTimeScreen(
    viewModel: SpeakTimeViewModel,
) {

    Scaffold(
        topBar = { SpeakTimeToolbar(viewModel = viewModel) },
        content = {
            SpeakTimeContent(
                viewModel = viewModel,
            )
        }
    )
}

