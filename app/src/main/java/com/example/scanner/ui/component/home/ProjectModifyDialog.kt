package com.example.scanner.ui.component.home

import android.widget.Space
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import com.example.scanner.ui.viewmodel.ProjectItemUiModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectModifyDialog(
    projectItemUiModel: ProjectItemUiModel?,
    onDialogVisibleChanged: () -> Unit
) {

    ModalBottomSheet(
        onDismissRequest = onDialogVisibleChanged,
        modifier = Modifier.fillMaxHeight()
    ){
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Name") },
            singleLine = true,
            placeholder = {
                if ( projectItemUiModel == null )
                    Text("Project Name")
                else
                    Text(projectItemUiModel.projectName)
                          },
            modifier = Modifier.padding(16.dp).fillMaxWidth()
        )
    }
}