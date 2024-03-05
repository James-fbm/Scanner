package com.example.scanner.ui.component.project

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.scanner.ui.viewmodel.ExcelImportUiModel

@Composable
fun ExcelImportDialog(
    excelImportUiModel: ExcelImportUiModel,
    onHeaderCheckedStateChanged: (Int) -> Unit,
    onDialogVisibleChanged: () -> Unit,
    onExcelImportAliasChanged: (String) -> Unit,
    onImportRequestSubmitted: (ExcelImportUiModel) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenHeightDp = configuration.screenHeightDp

    if (excelImportUiModel.dialogVisible) {
        Dialog(
            onDismissRequest = { },
        ) {
            val configuration = LocalConfiguration.current
            val maxHeightDp = Dp(configuration.screenHeightDp.toFloat())

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .wrapContentHeight(),
                shape = RoundedCornerShape(16.dp),
            ) {
                Text(
                    text = "Header Selector",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .align(Alignment.CenterHorizontally)
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = (screenHeightDp * 0.40f).dp)
                        .padding(top = 8.dp, bottom = 4.dp)
                ) {
                    items(excelImportUiModel.headerCheckedList.size) { index ->
                        val (header, checked) = excelImportUiModel.headerCheckedList[index]
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(header, modifier = Modifier.fillMaxWidth(0.8f))
                            Checkbox(
                                checked = checked,
                                onCheckedChange = {
                                    onHeaderCheckedStateChanged(index)
                                }
                            )
                        }
                    }
                }

                Divider()

                Text(
                    text = "Collection Alias",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .align(Alignment.CenterHorizontally)
                )


                OutlinedTextField(
                    value = excelImportUiModel.collectionAlias,
                    onValueChange = {input ->
                        onExcelImportAliasChanged(input)
                    },
                    label = { Text("imported as ") },
                    singleLine = true,
                    placeholder = {
                        Text("")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 8.dp),
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
                            onImportRequestSubmitted(excelImportUiModel)
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