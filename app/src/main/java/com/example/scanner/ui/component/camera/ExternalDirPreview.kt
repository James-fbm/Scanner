package com.example.scanner.ui.component.camera

import android.graphics.BitmapFactory
import android.os.Environment
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun ExternalDirPreview() {
    val context = LocalContext.current
    context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.let {extDir ->
        val fileArray = extDir.listFiles { file -> file.extension == "jpg" } ?: arrayOf()

        LazyColumn {
            items(fileArray.size) { index ->
                ListItem(
                    modifier = Modifier.fillMaxWidth(),
                    leadingContent = {
                        val bitmap = remember { BitmapFactory.decodeFile(fileArray[index].path) }.asImageBitmap()

                        Image(
                            bitmap = bitmap,
                            contentDescription = "",
                            modifier = Modifier.size(40.dp)
                        )
                    },
                    headlineContent = { Text(fileArray[index].name) }
                )
            }
        }
    }
}