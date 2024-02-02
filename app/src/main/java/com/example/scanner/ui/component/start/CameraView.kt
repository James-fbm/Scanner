package com.example.scanner.ui.component.start

import android.os.Environment
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.Executors

@Composable
fun CameraView(
    boxAlignment: Alignment,
    onTakePictureFinished: () -> Unit
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
                val externalFilesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                val fileDir = File(externalFilesDir, "img_${System.currentTimeMillis()}.jpg")

                val fileOption = ImageCapture.OutputFileOptions.Builder(fileDir).build()
                imageCapture.takePicture(fileOption, cameraExecutor,
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onError(error: ImageCaptureException) {
                            println("error: ${error.message}")
                        }
                        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                            println("saved at: ${fileDir.absolutePath}")
                            CoroutineScope(Dispatchers.Main).launch {
                                onTakePictureFinished()
                            }
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
