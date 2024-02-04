package com.example.scanner.ui.component.project

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
import com.example.scanner.ui.viewmodel.CollectionEditUiModel
import com.example.scanner.ui.viewmodel.CollectionItemUiModel

// ModalBottomSheet has trouble with keyboard imePadding. DO NOT USE IT.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionEditDialog(
    collectionEditUiModel: CollectionEditUiModel,

    // share the same visible change function with collection item button
    // we do not have and also do not need CollectionItemUiModel. This is just a declaration
    onDialogVisibleChanged: (CollectionItemUiModel?) -> Unit,
    onDialogCollectionNameInputChanged: (String) -> Unit,
    onEditRequestSubmitted: (CollectionEditUiModel) -> Unit
) {

    if (collectionEditUiModel.dialogVisible) {
        Dialog (
            onDismissRequest = { },
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Text(
                    text = "Edit Collection",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .align(Alignment.CenterHorizontally)
                )

                OutlinedTextField(
                    value = collectionEditUiModel.collectionName,
                    onValueChange = {inputName ->
                        onDialogCollectionNameInputChanged(inputName)
                    },
                    label = { Text("Name") },
                    singleLine = true,
                    placeholder = {
                        Text(collectionEditUiModel.collectionName)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
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
                            onEditRequestSubmitted(collectionEditUiModel)
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