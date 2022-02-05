package com.ctrlaccess.speaktime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.ctrlaccess.speaktime.ui.SpeakTimeScreen
import com.ctrlaccess.speaktime.ui.theme.SpeakTimeTheme
import com.ctrlaccess.speaktime.ui.viewModels.SpeakTimeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: SpeakTimeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getSpeakTimeSchedule()
        setContent {
            SpeakTimeTheme {
                SpeakTimeScreen(viewModel = viewModel)
            }
        }
    }




}
