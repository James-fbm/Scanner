package com.example.scanner.ui.component.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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

    when(homeUiState) {
        HomeUiState.Error -> Error()
        HomeUiState.Loading -> Loading()
        is HomeUiState.Success -> {
            Scaffold(
                topBar = {
                    TopNavigator(
                        topSearchBarUiModel =
                        (homeUiState as HomeUiState.Success).topSearchBarUiModel,
                        onSearchBarActiveChanged = {
                            homeViewModel.switchTopSearchBarActiveState()
                        },
                        onSearchBarInputChanged = { topSearchBarInput ->
                            homeViewModel.updateTopSearchBarInput(topSearchBarInput)
                        }
                    )
                },
                bottomBar = {
                    BottomButtonGroup(
                        projectItemDeleteEnabled =
                        (homeUiState as HomeUiState.Success).projectItemDeleteEnabled
                    )
                    ProjectModifyDialog(
                        null,
                        {}
                    )
                }
            ) {innerPadding ->
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(innerPadding).padding(top = 12.dp)
                ) {
                    ProjectDisplayBody(
                        allProjectItemCheckedState = (homeUiState as HomeUiState.Success).allProjectItemCheckedState,
                        projectItemUiModelList = (homeUiState as HomeUiState.Success).projectItemUiModelList,
                        onAllProjectItemCheckedStateChanged = { checkedState ->
                            homeViewModel.switchAllProjectItemCheckedState(checkedState)
                        },
                        onItemCheckedChanged = { projectItemUiModel ->
                            homeViewModel.switchProjectItemCheckedState(projectItemUiModel)
                        },
                        onMenuVisibleChanged = { projectItemUiModel ->
                            homeViewModel.switchProjectItemMenuVisibility(projectItemUiModel)
                        }
                    )
                }
            }
        }
    }
}