package com.ctrlaccess.speaktime.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ctrlaccess.speaktime.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navigate: () -> Unit
) {

    LaunchedEffect(key1 = true) {
        delay(1000)
        navigate()
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Red)

    ) {
        Image(
            modifier = Modifier
                .align(alignment = Alignment.Center)
                .width(250.dp)
                .height(250.dp),
            painter = painterResource(id = R.drawable.ic_watch),
            contentDescription = stringResource(id = R.string.splash_screen)
        )
    }


}

@Preview(showBackground = true)
@Composable
fun PreviewSplashScreen() {
    SplashScreen({})
}