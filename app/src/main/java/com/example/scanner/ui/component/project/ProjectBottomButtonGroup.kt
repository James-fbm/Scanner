package com.example.scanner.ui.component.project

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.scanner.R

@Composable
fun ProjectBottomButtonGroup(
    collectionItemDeleteEnabled: Boolean,
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
                enabled = collectionItemDeleteEnabled,
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
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = {}
            ) {
                Image(
                    painter = painterResource(id = R.drawable.folder),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}