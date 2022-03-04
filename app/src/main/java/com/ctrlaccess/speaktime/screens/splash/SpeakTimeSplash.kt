package com.ctrlaccess.speaktime.screens.splash

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ctrlaccess.speaktime.R
import com.ctrlaccess.speaktime.ui.theme.SpeakTimeTheme
import com.ctrlaccess.speaktime.ui.theme.backgroundColor2
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navigate: () -> Unit
) {

    LaunchedEffect(key1 = true) {
        delay(300)
        navigate()
    }

    val img = if (isSystemInDarkTheme()) R.drawable.ic_watch_white else R.drawable.ic_watch_black


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = backgroundColor2)

    ) {
        Image(
            modifier = Modifier
                .align(alignment = Alignment.Center)
                .width(250.dp)
                .height(250.dp),
            painter = painterResource(id = img),
            contentDescription = stringResource(id = R.string.splash_screen)
        )
    }

}


@Preview(
    showBackground = true,
    name = "Night Mode",
    uiMode = UI_MODE_NIGHT_YES
)
@Preview(
    showBackground = true,
    name = "Day Mode",
    uiMode = UI_MODE_NIGHT_NO
)
@Composable
fun PreviewSplashScreen() {

    SpeakTimeTheme {
        SplashScreen({})
    }
}