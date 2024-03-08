package com.example.scanner.ui.component.collection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.scanner.ui.viewmodel.VolumeDeleteUiModel


@Composable
fun VolumeDeleteDialog(
    volumeDeleteUiModel: VolumeDeleteUiModel,
    onDialogVisibleChanged: () -> Unit,
    onDeleteRequestSubmitted: () -> Unit
) {
    if (volumeDeleteUiModel.dialogVisible) {
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
                    text = "Delete Volume",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .align(Alignment.CenterHorizontally)
                )

                Text(
                    text = "Deleting volumes will also remove all contents within the volumes.",
                    modifier = Modifier
                        .padding(top = 16.dp, start = 16.dp)
                )

                Text(
                    text = "The action is irreversible.",
                    modifier = Modifier
                        .padding(top = 16.dp, start = 16.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = {
                            onDialogVisibleChanged()
                        }
                    ) {
                        Text("Cancel", style = MaterialTheme.typography.labelLarge)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    TextButton(
                        onClick = {
                            onDeleteRequestSubmitted()
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