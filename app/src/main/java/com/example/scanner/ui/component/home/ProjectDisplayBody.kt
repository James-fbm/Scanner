package com.example.scanner.ui.component.home

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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import com.example.scanner.ui.viewmodel.ProjectListItemUiModel

@Composable
fun ProjectDisplayBody(
    allProjectListItemCheckedState: ToggleableState,
    projectListItemUiModelList: List<ProjectListItemUiModel>,
    onAllProjectListItemCheckedStateChanged: (ToggleableState) -> Unit,
    onItemCheckedChanged: (ProjectListItemUiModel) -> Unit,
    onMenuVisibleChanged: (ProjectListItemUiModel) -> Unit
) {

    // Select all checkbox and filter button on the top of the list

    Row ( modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        TriStateCheckbox(
            state = allProjectListItemCheckedState,
            onClick = {
                onAllProjectListItemCheckedStateChanged(allProjectListItemCheckedState)
            })
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
    // list of all the project items

    LazyColumn(
        modifier = Modifier.fillMaxHeight()
    ) {
        items(count = projectListItemUiModelList.size) { index ->
            ProjectListItem (
                projectListItemUiModel = projectListItemUiModelList[index],
                onItemCheckedChanged = onItemCheckedChanged,
                onMenuVisibleChanged = onMenuVisibleChanged
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}