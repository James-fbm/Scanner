package com.example.scanner.ui.component.collection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.scanner.ui.viewmodel.VolumeViewUiModel

@Composable
fun VolumeViewDialog(
    volumeViewUiModel: VolumeViewUiModel,
    onDialogVisibleChanged: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenHeightDp = configuration.screenHeightDp
    if (volumeViewUiModel.dialogVisible) {
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
                    text = "Volume Properties View",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(top = 16.dp, bottom = 12.dp)
                        .align(Alignment.CenterHorizontally)
                )

                if (volumeViewUiModel.fromExcel) {
                    Text(
                        text = "Source Examples",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier
                            .padding(top = 8.dp, bottom = 8.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    LazyColumn(
                        modifier = Modifier.heightIn(max = (screenHeightDp * 0.75f).dp)
                    ) {
                        items(count = volumeViewUiModel.volumeSource.size) {listIndex ->
                            Column {
                                for (sourceTriple in volumeViewUiModel.volumeSource[listIndex]) {
                                    // if the title is selected as index, make it bold
                                    if (sourceTriple.third) {
                                        Text(
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Bold,
                                            text = "${sourceTriple.first}: ${sourceTriple.second}"
                                        )
                                    } else {
                                        Text(
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Normal,
                                            text = "${sourceTriple.first}: ${sourceTriple.second}"
                                        )
                                    }
                                }
                            }
                            Divider(
                                modifier = Modifier.padding(top = 6.dp, bottom = 6.dp)
                            )
                        }

                    }
                } else {
                    Text(
                        text = "Imported Manually",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier
                            .padding(top = 8.dp, bottom = 8.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }

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
                }
            }
        }
    }
}