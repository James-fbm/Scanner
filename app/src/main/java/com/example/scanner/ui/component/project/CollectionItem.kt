package com.example.scanner.ui.component.project

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scanner.ui.viewmodel.CollectionItemUiModel

@Composable
fun CollectionItem (
    collectionItemUiModel: CollectionItemUiModel,
    onItemClicked: (Int) -> Unit,
    onItemCheckedChanged: (CollectionItemUiModel) -> Unit,
    onMenuVisibleChanged: (CollectionItemUiModel) -> Unit,
    onEditDialogVisibleChanged: (CollectionItemUiModel?) -> Unit,
) {
    ListItem(
        modifier = Modifier.clickable {
            onItemClicked(collectionItemUiModel.collectionId)
        },
        headlineContent = {
            Text(collectionItemUiModel.collectionName)
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
                    text = collectionItemUiModel.modifyTime,
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
        },
        leadingContent = {
            Checkbox(
                checked = collectionItemUiModel.itemChecked,
                onCheckedChange = {
                    onItemCheckedChanged(collectionItemUiModel)
                }
            )
        },
        trailingContent = {
            IconButton(
                onClick = {
                    onMenuVisibleChanged(collectionItemUiModel)
                }
            ) {
                Icon(
                    Icons.Outlined.MoreVert,
                    contentDescription = null
                )
                DropdownMenu(
                    expanded = collectionItemUiModel.menuVisible,
                    onDismissRequest = {
                        onMenuVisibleChanged(collectionItemUiModel)
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
                            onEditDialogVisibleChanged(collectionItemUiModel)
                        }
                    )
                    // Add more items as needed
                }
            }
        }
    )
}