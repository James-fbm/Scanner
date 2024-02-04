package com.example.scanner.ui.component.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeBottomButtonGroup(
    projectItemDeleteEnabled: Boolean,
    onAddDialogVisibleChanged: () -> Unit,
    onDeleteDialogVisibleChanged: () -> Unit
) {
    BottomAppBar {
        Row ( modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(
                enabled = projectItemDeleteEnabled,
                onClick = {
                    onDeleteDialogVisibleChanged()
                }
            ) {
                Icon(Icons.Filled.Delete, contentDescription = null)
            }
            Spacer(modifier = Modifier.weight(1f))
            FloatingActionButton(
                onClick = { onAddDialogVisibleChanged() },
                containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
            ) {
                Icon(Icons.Filled.Add, contentDescription = null)
            }
        }
    }
}