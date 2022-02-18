package com.ctrlaccess.speaktime.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)

val Color1 = Color(0xE9ECEFFF)
val Color2 = Color(0x212529FF)
val Color3 = Color(0x212529FF)
val Color4 = Color(0xE9ECEFFF)
val Color5 = Color(0xB80C09FF)

val borderColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color.White else Color.DarkGray

val backgroundColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color.DarkGray else Color.White

val backgroundColor1: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color2 else Color.White

val textColor1: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color.White else Color.Black

val outlinedButtonBorder: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color1 else Color2

val outlinedButtonBackground: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color3 else Color4

