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
import com.example.scanner.ui.viewmodel.ProjectItemUiModel

@Composable
fun HomeMain(
    homeViewModel: HomeViewModel,
    onProjectItemClicked: (Int) -> Unit
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
                        (homeUiState as HomeUiState.Success).projectItemDeleteEnabled,
                        onAddDialogVisibleChanged = {
                            homeViewModel.switchProjectAddDialogVisibility()
                        }
                    )
                }
            ) {innerPadding ->
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(innerPadding).padding(top = 12.dp)
                ) {
                    ProjectDisplayBody(
                        allProjectItemCheckedState = (homeUiState as HomeUiState.Success).allProjectItemCheckedState,
                        projectAddUiModel = (homeUiState as HomeUiState.Success).projectAddUiModel,
                        projectEditUiModel = (homeUiState as HomeUiState.Success).projectEditUiModel,
                        projectItemUiModelList = (homeUiState as HomeUiState.Success).projectItemUiModelList,
                        onItemClicked = onProjectItemClicked,
                        onAllProjectItemCheckedStateChanged = { checkedState ->
                            homeViewModel.switchAllProjectItemCheckedState(checkedState)
                        },
                        onItemCheckedChanged = { projectItemUiModel ->
                            homeViewModel.switchProjectItemCheckedState(projectItemUiModel)
                        },
                        onMenuVisibleChanged = { projectItemUiModel ->
                            homeViewModel.switchProjectItemMenuVisibility(projectItemUiModel)
                        },
                        onEditDialogVisibleChanged = {projectItemUiModel ->  
                            homeViewModel.switchProjectEditDialogVisibility(projectItemUiModel)
                        },
                        onAddDialogVisibleChanged = {
                            homeViewModel.switchProjectAddDialogVisibility()
                        }
                    )
                }
            }
        }
    }
}