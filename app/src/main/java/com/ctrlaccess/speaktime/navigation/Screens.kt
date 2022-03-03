package com.ctrlaccess.speaktime.navigation

sealed class Screens(val route: String){

    object SplashScreen:Screens(route = "splash_screen")
    object MainScreen:Screens(route = "main_screen")
}
