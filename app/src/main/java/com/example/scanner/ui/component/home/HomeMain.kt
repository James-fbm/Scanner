package com.example.scanner.ui.component.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.List
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.scanner.ui.viewmodel.HomeUiState
import com.example.scanner.ui.viewmodel.HomeViewModel
import com.example.scanner.ui.viewmodel.ProjectListItemUiModel

@Composable
fun HomeMain(
    homeViewModel: HomeViewModel
) {
    val homeUiState by homeViewModel.homeUiState.collectAsState()
    
    LaunchedEffect(Unit) {
        homeViewModel.getProjectList()
    }
    
    Scaffold(
        topBar = { TopNavigator()}
    ) {innerPadding ->
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.padding(innerPadding))

            when(homeUiState) {
                HomeUiState.Error -> Error()
                HomeUiState.Loading -> Loading()
                is HomeUiState.Success -> ProjectList(
                    (homeUiState as HomeUiState.Success).projectListItemUiModelList,
                    onItemCheckedChange = { projectListItemUiModel ->
                        homeViewModel.changeProjectItemCheckedStatus(projectListItemUiModel)
                    }
                )
            }

            IconButton(
                modifier = Modifier
                    .height(64.dp)
                    .width(96.dp)
                    .align(Alignment.End),
                onClick = {}
            ) {
                Icon(
                    Icons.Outlined.List,
                    contentDescription = null,
                )
            }
        }
    }
}