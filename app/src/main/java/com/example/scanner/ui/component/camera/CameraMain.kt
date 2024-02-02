package com.example.scanner.ui.component.camera

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalConfiguration


@Composable
fun CameraMain(

    // should be a navigation operation
    // switch to another page after successfully taking shots
    onTakePictureFinished: () -> Unit
) {

    val isLandscape = LocalConfiguration.current.orientation

    val cameraBoxAlignment = if (isLandscape == Configuration.ORIENTATION_LANDSCAPE) {
        Alignment.CenterEnd
    } else {
        Alignment.BottomCenter
    }

    CameraView(
        boxAlignment = cameraBoxAlignment,
        onTakePictureFinished = onTakePictureFinished
    )
}