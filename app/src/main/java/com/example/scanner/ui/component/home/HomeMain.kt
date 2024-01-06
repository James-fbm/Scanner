package com.example.scanner.ui.component.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.List
import androidx.compose.material3.Checkbox
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
            Spacer(modifier = Modifier.height(12.dp))

            when(homeUiState) {
                HomeUiState.Error -> Error()
                HomeUiState.Loading -> Loading()
                is HomeUiState.Success ->  {
                    Row ( modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Checkbox(
                            checked = (homeUiState as HomeUiState.Success).allProjectListItemCheckedState,
                            onCheckedChange = {checkedState ->
                                homeViewModel.switchAllProjectListItemCheckedState(checkedState)
                                println("checked state: $checkedState")
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

                    ProjectList(
                        (homeUiState as HomeUiState.Success).projectListItemUiModelList,
                        onItemCheckedChanged = { projectListItemUiModel ->
                            homeViewModel.switchProjectListItemCheckedState(projectListItemUiModel)
                        },
                        onMenuVisibleChanged = { projectListItemUiModel ->
                            homeViewModel.switchProjectListItemMenuVisibility(projectListItemUiModel)
                        }
                    )
                }
            }


        }
    }
}