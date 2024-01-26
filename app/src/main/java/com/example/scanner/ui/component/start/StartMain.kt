package com.example.scanner.ui.component.start

import android.content.res.Configuration
import android.util.Log
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.List
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getExternalFilesDirs
import java.io.File
import java.util.concurrent.Executors


@Composable
fun StartMain(
) {

    val isLandscape = LocalConfiguration.current.orientation

    val boxAlignment = if (isLandscape == Configuration.ORIENTATION_LANDSCAPE) {
        Alignment.CenterEnd
    } else {
        Alignment.BottomCenter
    }

    CameraView(
        boxAlignment = boxAlignment
    )
}

@Composable
fun CameraView(
    boxAlignment: Alignment
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val imageCapture = remember{ ImageCapture.Builder().build() }
    val cameraExecutor = Executors.newSingleThreadExecutor()

    Box(contentAlignment = boxAlignment) {
        AndroidView(
            factory = {context ->

            val previewView = PreviewView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                this.scaleType = scaleType
            }

            val previewUseCase = Preview.Builder().build()
            previewUseCase.setSurfaceProvider(previewView.surfaceProvider)

            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    imageCapture,
                    previewUseCase
                )
            }, ContextCompat.getMainExecutor(context))

            previewView
        })

        IconButton(
            onClick = {
                val externalFilesDir = context.getExternalFilesDir(null)
                val fileDir = File(externalFilesDir, "img_${System.currentTimeMillis()}.jpg")

                val fileOption = ImageCapture.OutputFileOptions.Builder(fileDir).build()
                imageCapture.takePicture(fileOption, cameraExecutor,
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onError(error: ImageCaptureException) {
                            println("error: ${error.message}")
                        }
                        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                            println("saved at: ${fileDir.absolutePath}")
                        }
                    })
            },
            content = {
                Icon(
                    Icons.Filled.Check,
                    contentDescription = null,
                )
            },
            modifier = Modifier
                .padding(24.dp)
                .background(
                    color = Color.LightGray,
                    shape = CircleShape
                )
        )
    }

}
