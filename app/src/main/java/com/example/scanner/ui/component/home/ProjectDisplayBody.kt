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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import com.example.scanner.ui.viewmodel.ProjectAddUiModel
import com.example.scanner.ui.viewmodel.ProjectEditUiModel
import com.example.scanner.ui.viewmodel.ProjectItemUiModel

@Composable
fun ProjectDisplayBody(
    allProjectItemCheckedState: ToggleableState,
    projectAddUiModel: ProjectAddUiModel,
    projectEditUiModel: ProjectEditUiModel,
    projectItemUiModelList: List<ProjectItemUiModel>,
    onItemClicked: (Int) -> Unit,
    onAllProjectItemCheckedStateChanged: (ToggleableState) -> Unit,
    onItemCheckedChanged: (ProjectItemUiModel) -> Unit,
    onMenuVisibleChanged: (ProjectItemUiModel) -> Unit,
    onEditDialogVisibleChanged: (ProjectItemUiModel?) -> Unit,
    onAddDialogVisibleChanged: () -> Unit
) {
    ProjectEditDialog(projectEditUiModel = projectEditUiModel) { projectItemUiModel ->
        onEditDialogVisibleChanged(projectItemUiModel)
    }
    
    ProjectAddDialog(projectAddUiModel = projectAddUiModel) {
        onAddDialogVisibleChanged()
    }

    // Select all checkbox and filter button on the top of the list

    Row ( modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        TriStateCheckbox(
            state = allProjectItemCheckedState,
            onClick = {
                onAllProjectItemCheckedStateChanged(allProjectItemCheckedState)
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
        items(count = projectItemUiModelList.size) { index ->
            ProjectItem (
                projectItemUiModel = projectItemUiModelList[index],
                onItemClicked = onItemClicked,
                onItemCheckedChanged = onItemCheckedChanged,
                onMenuVisibleChanged = onMenuVisibleChanged,
                onEditDialogVisibleChanged = onEditDialogVisibleChanged
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}