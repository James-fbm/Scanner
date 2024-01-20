package com.example.scanner.ui.component.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scanner.ui.viewmodel.ProjectItemUiModel

@Composable
fun ProjectItem (
    projectItemUiModel: ProjectItemUiModel,
    onItemClicked: (Int) -> Unit,
    onItemCheckedChanged: (ProjectItemUiModel) -> Unit,
    onMenuVisibleChanged: (ProjectItemUiModel) -> Unit,
    onEditDialogVisibleChanged: (ProjectItemUiModel?) -> Unit,
) {
    ListItem(
        modifier = Modifier.clickable {
            onItemClicked(projectItemUiModel.projectId)
        },
        headlineContent = {
            Text(projectItemUiModel.projectName)
        },
        supportingContent = {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Outlined.Edit,
                    contentDescription = null,
                    modifier = Modifier.height(12.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = projectItemUiModel.modifyTime,
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
        },
        leadingContent = {
            Checkbox(
                checked = projectItemUiModel.itemChecked,
                onCheckedChange = {
                    onItemCheckedChanged(projectItemUiModel)
                }
            )
        },
        trailingContent = {
            IconButton(
                onClick = {
                    onMenuVisibleChanged(projectItemUiModel)
                }
            ) {
                Icon(
                    Icons.Outlined.MoreVert,
                    contentDescription = null
                )
                DropdownMenu(
                    expanded = projectItemUiModel.menuVisible,
                    onDismissRequest = {
                        onMenuVisibleChanged(projectItemUiModel)
                    }
                ) {
                    DropdownMenuItem(
                        text = { Text("Edit") },
                        leadingIcon = {
                            Icon(
                                Icons.Outlined.Edit,
                                contentDescription = null
                            )},
                        onClick = {
                            onEditDialogVisibleChanged(projectItemUiModel)
                        }
                    )
                    // Add more items as needed
                }
            }
        }
    )
}