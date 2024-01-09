package com.example.scanner.ui.component.home

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavigator (
    topSearchBarInput: String,
    onSearchBarInputChanged: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ){
        IconButton(onClick = {}) {
            Icon(Icons.Outlined.ArrowBack, contentDescription = null)
        }
        SearchBar(
            active = false,
            onActiveChange = {},
            onQueryChange = {input ->
                onSearchBarInputChanged(input)
            },
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
            query = topSearchBarInput
        ) {

        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun TopNavigatorPreview() {
    TopNavigator("Project", {})
}