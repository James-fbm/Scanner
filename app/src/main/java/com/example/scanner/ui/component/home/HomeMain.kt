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
import com.example.scanner.ui.component.Error
import com.example.scanner.ui.component.Loading
import com.example.scanner.ui.viewmodel.HomeUiState
import com.example.scanner.ui.viewmodel.HomeViewModel

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
                        },
                        onDeleteDialogVisibleChanged = {
                            homeViewModel.switchProjectDeleteDialogVisibility()
                        }
                    )
                }
            ) {innerPadding ->
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(top = 12.dp)
                ) {
                    ProjectDisplayBody(
                        allProjectItemCheckedState = (homeUiState as HomeUiState.Success).allProjectItemCheckedState,
                        projectItemUiModelList = (homeUiState as HomeUiState.Success).projectItemUiModelList,
                        onItemClicked = onProjectItemClicked,
                        onAllItemCheckedStateChanged = { checkedState ->
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
                        }
                    )

                    ProjectEditDialog(
                        projectEditUiModel = (homeUiState as HomeUiState.Success).projectEditUiModel,
                        onDialogVisibleChanged = { projectItemUiModel ->
                            homeViewModel.switchProjectEditDialogVisibility(projectItemUiModel)
                        },
                        onDialogProjectNameInputChanged = {inputName ->
                            homeViewModel.updateEditDialogProjectNameInput(inputName)
                        },
                        onEditRequestSubmitted = {projectEditUiModel ->
                            homeViewModel.submitUpdateProject(projectEditUiModel)
                        }
                    )

                    ProjectAddDialog(
                        projectAddUiModel = (homeUiState as HomeUiState.Success).projectAddUiModel,
                        onDialogVisibleChanged = {
                            homeViewModel.switchProjectAddDialogVisibility()
                        },
                        onDialogProjectNameInputChanged = {inputName ->
                            homeViewModel.updateAddDialogProjectNameInput(inputName)
                        },
                        onAddRequestSubmitted = { projectAddUiModel ->
                            homeViewModel.submitAddProject(projectAddUiModel)
                        }
                    )

                    ProjectDeleteDialog(
                        projectDeleteUiModel = (homeUiState as HomeUiState.Success).projectDeleteUiModel,
                        onDialogVisibleChanged = {
                            homeViewModel.switchProjectDeleteDialogVisibility()
                        },
                        onDeleteRequestSubmitted = {
                            homeViewModel.submitDeleteProject()
                        }
                    )
                }
            }
        }
    }
}