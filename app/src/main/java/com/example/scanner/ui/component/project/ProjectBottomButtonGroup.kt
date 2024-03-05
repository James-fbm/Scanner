package com.example.scanner.ui.component.project

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.DocumentsContract
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.scanner.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream


@Composable
fun ProjectBottomButtonGroup(
    collectionItemDeleteEnabled: Boolean,
    onAddDialogVisibleChanged: () -> Unit,
    onDeleteDialogVisibleChanged: () -> Unit,
    parseExcelHeader: suspend (Pair<String?, String?>) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val fileSelector = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri: Uri? = result.data?.data

            coroutineScope.launch(Dispatchers.IO) {
                val fileMeta = copyExcelFileToCache(uri, context)
                parseExcelHeader(fileMeta)
            }
        }
    }

    BottomAppBar {
        Row ( modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(
                enabled = collectionItemDeleteEnabled,
                onClick = {
                    onDeleteDialogVisibleChanged()
                }
            ) {
                Icon(Icons.Filled.Delete, contentDescription = null)
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = {
                    val pickerInitialUri: Uri? = null
                    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                        addCategory(Intent.CATEGORY_OPENABLE)
                        type = "*/*"
                        putExtra(Intent.EXTRA_MIME_TYPES, arrayOf(
                            "application/csv",
                            "application/x-csv",
                            "text/csv",
                            "text/x-csv",
                            "text/comma-separated-values",
                            "text/x-comma-separated-values",
                            "application/vnd.ms-excel",
                            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                        ))
                        putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri)
                    }
                    fileSelector.launch(intent)
            }) {
                Image(
                    painter = painterResource(id = R.drawable.folder),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            FloatingActionButton(
                onClick = { onAddDialogVisibleChanged() },
                containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
            ) {
                Icon(Icons.Filled.Add, contentDescription = null)
            }
        }
    }
}

suspend fun copyExcelFileToCache(uri: Uri?, context: Context): Pair<String?, String?> {

    uri ?: return Pair(null, null)
    val contentResolver = context.contentResolver ?: return Pair(null, null)
    val uriPath = uri.encodedPath ?: return Pair(null, null)
    val pathElement = uriPath.split("/")
    val fileName = pathElement[pathElement.size - 1]

    val fileType: String = when (contentResolver.getType(uri)) {
        "application/vnd.ms-excel" -> "xls"
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" -> "xlsx"
        else -> "csv"
    }

    context.contentResolver.openInputStream(uri)?.use { inputStream ->
        val newFile = File(context.externalCacheDir, fileName)
        FileOutputStream(newFile).use { fileOutputStream ->
            inputStream.copyTo(fileOutputStream)
        }
        return Pair(newFile.absolutePath, fileType)
    }

    return Pair(null, null)
}
