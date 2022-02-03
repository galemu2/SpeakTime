package com.ctrlaccess.speaktime.ui

import android.util.Log
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.ctrlaccess.speaktime.ui.viewModels.SpeakTimeViewModel
import com.ctrlaccess.speaktime.util.Const.TAG


@Composable
fun SpeakTimeScreen(viewModel: SpeakTimeViewModel) {

    LaunchedEffect(key1 = true){
        Log.d(TAG, "SpeakTimeScreen: ")
    }
    Scaffold(
        topBar = { SpeakTimeToolbar() },
        content = {
            SpeakTimeContent(
                viewModel = viewModel,
            )
        }
    )
}

