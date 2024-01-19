package com.example.scanner.ui.component.home

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Clear
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
import androidx.compose.ui.unit.dp
import com.example.scanner.ui.viewmodel.TopSearchBarUiModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavigator (
    topSearchBarUiModel: TopSearchBarUiModel,
    onSearchBarActiveChanged: () -> Unit,
    onSearchBarInputChanged: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 12.dp)
    ){
        SearchBar(
            modifier = Modifier.fillMaxWidth(),
            active = topSearchBarUiModel.activeState,
            onActiveChange = {
                onSearchBarActiveChanged()
            },
            onQueryChange = {input ->
                onSearchBarInputChanged(input)
            },
            onSearch = {

            },
            placeholder = { Text("Search") },
            leadingIcon = {
                if (!topSearchBarUiModel.activeState) {
                    Icon(Icons.Outlined.Search, contentDescription = null)
                } else {
                    IconButton(
                        onClick = {
                            onSearchBarInputChanged("")
                        }
                    ) {
                        Icon(Icons.Outlined.Clear, contentDescription = null)
                    }
                }},
            query = topSearchBarUiModel.inputQuery
        ) {

        }
    }
}