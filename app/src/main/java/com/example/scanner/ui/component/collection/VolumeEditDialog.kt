package com.example.scanner.ui.component.collection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.scanner.ui.viewmodel.VolumeEditUiModel
import com.example.scanner.ui.viewmodel.VolumeItemUiModel

// ModalBottomSheet has trouble with keyboard imePadding. DO NOT USE IT.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VolumeEditDialog(
    volumeEditUiModel: VolumeEditUiModel,

    // share the same visible change function with volume item button
    // we do not have and also do not need VolumeItemUiModel. This is just a declaration
    onDialogVisibleChanged: (VolumeItemUiModel?) -> Unit,
    onDialogVolumeNameInputChanged: (String) -> Unit,
    onEditRequestSubmitted: (VolumeEditUiModel) -> Unit
) {

    if (volumeEditUiModel.dialogVisible) {
        Dialog (
            onDismissRequest = { },
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Text(
                    text = "Edit Volume",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .align(Alignment.CenterHorizontally)
                )

                OutlinedTextField(
                    value = volumeEditUiModel.volumeName,
                    onValueChange = {inputName ->
                        onDialogVolumeNameInputChanged(inputName)
                    },
                    label = { Text("Name") },
                    singleLine = true,
                    placeholder = {
                        Text(volumeEditUiModel.volumeName)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = {
                            onDialogVisibleChanged(null)
                        }
                    ) {
                        Text("Cancel", style = MaterialTheme.typography.labelLarge)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    TextButton(
                        onClick = {
                            onEditRequestSubmitted(volumeEditUiModel)
                        }
                    ) {
                        Text("OK", style = MaterialTheme.typography.labelLarge)
                    }

                    Spacer(modifier = Modifier.width(16.dp))
                }
            }
        }
    }
}