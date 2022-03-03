package com.ctrlaccess.speaktime.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ctrlaccess.speaktime.navigation.Screens
import com.ctrlaccess.speaktime.screens.main.SpeakTimeScreen
import com.ctrlaccess.speaktime.screens.splash.SplashScreen
import com.ctrlaccess.speaktime.ui.theme.SpeakTimeTheme
import com.ctrlaccess.speaktime.ui.viewModels.SpeakTimeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: SpeakTimeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            SpeakTimeTheme {

                NavHost(
                    navController = navController,
                    startDestination = Screens.SplashScreen.route
                ) {
                    composable(
                        route = Screens.SplashScreen.route
                    ) {
                        SplashScreen(
                            navigate = {
                                navController.navigate(Screens.MainScreen.route)
                            }
                        )
                    }
                    composable(
                        route = Screens.MainScreen.route
                    ) {
                        SpeakTimeScreen(viewModel = viewModel)
                    }

                }

            }
        }
    }


}
