package com.example.scanner.ui.component.project

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
import com.example.scanner.ui.component.Loading
import com.example.scanner.ui.viewmodel.ProjectUiState
import com.example.scanner.ui.viewmodel.ProjectViewModel

@Composable
fun ProjectMain(
    projectViewModel: ProjectViewModel,
    onCollectionItemClicked: (Int) -> Unit
) {
    val projectUiState by projectViewModel.projectUiState.collectAsState()

    LaunchedEffect(Unit) {
        projectViewModel.getCollectionList()
    }

    when(projectUiState) {
        ProjectUiState.Error -> Error()
        ProjectUiState.Loading -> Loading()
        is ProjectUiState.Success -> {
            Scaffold(
                topBar = {
                    ProjectTopNavigator(
                        projectTopSearchBarUiModel =
                        (projectUiState as ProjectUiState.Success).projectTopSearchBarUiModel,
                        onSearchBarActiveChanged = {
                            projectViewModel.switchTopSearchBarActiveState()
                        },
                        onSearchBarInputChanged = { topSearchBarInput ->
                            projectViewModel.updateTopSearchBarInput(topSearchBarInput)
                        }
                    )
                },
                bottomBar = {
                    ProjectBottomButtonGroup(
                        collectionItemDeleteEnabled =
                        (projectUiState as ProjectUiState.Success).collectionItemDeleteEnabled,
                        onAddDialogVisibleChanged = {
                            projectViewModel.switchCollectionAddDialogVisibility()
                        },
                        onDeleteDialogVisibleChanged = {
                            projectViewModel.switchCollectionDeleteDialogVisibility()
                        },
                        parseExcelFile =  { fileMeta ->
                            projectViewModel.parseExcelFile(fileMeta)
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
                    CollectionDisplayBody(
                        allCollectionItemCheckedState = (projectUiState as ProjectUiState.Success).allCollectionItemCheckedState,
                        collectionItemUiModelList = (projectUiState as ProjectUiState.Success).collectionItemUiModelList,
                        onItemClicked = onCollectionItemClicked,
                        onAllItemCheckedStateChanged = { checkedState ->
                            projectViewModel.switchAllCollectionItemCheckedState(checkedState)
                        },
                        onItemCheckedChanged = { collectionItemUiModel ->
                            projectViewModel.switchCollectionItemCheckedState(collectionItemUiModel)
                        },
                        onMenuVisibleChanged = { collectionItemUiModel ->
                            projectViewModel.switchCollectionItemMenuVisibility(collectionItemUiModel)
                        },
                        onEditDialogVisibleChanged = {collectionItemUiModel ->
                            projectViewModel.switchCollectionEditDialogVisibility(collectionItemUiModel)
                        }
                    )
                }

                CollectionEditDialog(
                    collectionEditUiModel = (projectUiState as ProjectUiState.Success).collectionEditUiModel,
                    onDialogVisibleChanged = { collectionItemUiModel ->
                        projectViewModel.switchCollectionEditDialogVisibility(collectionItemUiModel)
                    },
                    onDialogCollectionNameInputChanged = {inputName ->
                        projectViewModel.updateEditDialogCollectionNameInput(inputName)
                    },
                    onEditRequestSubmitted = {collectionEditUiModel ->
                        projectViewModel.submitUpdateCollection(collectionEditUiModel)
                    }
                )

                CollectionAddDialog(
                    collectionAddUiModel = (projectUiState as ProjectUiState.Success).collectionAddUiModel,
                    onDialogVisibleChanged = {
                        projectViewModel.switchCollectionAddDialogVisibility()
                    },
                    onDialogCollectionNameInputChanged = {inputName ->
                        projectViewModel.updateAddDialogCollectionNameInput(inputName)
                    },
                    onAddRequestSubmitted = { collectionAddUiModel ->
                        projectViewModel.submitAddCollection(collectionAddUiModel)
                    }
                )

                CollectionDeleteDialog(
                    collectionDeleteUiModel = (projectUiState as ProjectUiState.Success).collectionDeleteUiModel,
                    onDialogVisibleChanged = {
                        projectViewModel.switchCollectionDeleteDialogVisibility()
                    },
                    onDeleteRequestSubmitted = {
                        projectViewModel.submitDeleteCollection()
                    }
                )
            }
        }
    }
}