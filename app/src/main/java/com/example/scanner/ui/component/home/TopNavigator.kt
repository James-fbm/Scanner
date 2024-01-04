package com.example.scanner.ui.component.home

import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavigator () {
    TopAppBar(
        navigationIcon = {
            IconButton(
                modifier = Modifier.height(64.dp),
                onClick = {}
            ) {
                Icon(
                    Icons.Outlined.ArrowBack,
                    contentDescription = null,
                )
            }
        },
        title = {
            SearchBar(
                active = false,
                onActiveChange = {},
                onQueryChange = {},
                onSearch = {},
                placeholder = { Text("Search") },
                leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null) },
                trailingIcon = {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(Icons.Outlined.Menu, contentDescription = null)
                    }
                },
                query = ""
            ) {

            }
        }
    )
}

@Preview
@Composable
fun TopNavigatorPreview() {
    TopNavigator()
}