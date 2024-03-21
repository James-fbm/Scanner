package com.example.scanner.ui.component.collection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.List
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import com.example.scanner.ui.viewmodel.VolumeItemUiModel

@Composable
fun VolumeDisplayBody(
    allVolumeItemCheckedState: ToggleableState,
    volumeItemUiModelList: List<VolumeItemUiModel>,
    onItemClicked: (Int) -> Unit,
    onAllItemCheckedStateChanged: (ToggleableState) -> Unit,
    onItemCheckedChanged: (VolumeItemUiModel) -> Unit,
    onMenuVisibleChanged: (VolumeItemUiModel) -> Unit,
    onEditDialogVisibleChanged: (VolumeItemUiModel?) -> Unit,
    onViewDialogVisibleChanged: (VolumeItemUiModel?) -> Unit
) {

    // Select all checkbox and filter button on the top of the list

    Row ( modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        TriStateCheckbox(
            state = allVolumeItemCheckedState,
            onClick = {
                onAllItemCheckedStateChanged(allVolumeItemCheckedState)
            })
        Spacer(modifier = Modifier.weight(1f))
        Text("Volume List")
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            onClick = {}
        ) {
            Icon(
                Icons.Outlined.List,
                contentDescription = null,
            )
        }
    }

    Spacer(modifier = Modifier.height(16.dp))
    // list of all the volume items

    LazyColumn(
        modifier = Modifier.fillMaxHeight()
    ) {
        items(count = volumeItemUiModelList.size) { index ->
            VolumeItem (
                volumeItemUiModel = volumeItemUiModelList[index],
                onItemClicked = onItemClicked,
                onItemCheckedChanged = onItemCheckedChanged,
                onMenuVisibleChanged = onMenuVisibleChanged,
                onEditDialogVisibleChanged = onEditDialogVisibleChanged,
                onViewDialogVisibleChanged = onViewDialogVisibleChanged
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}