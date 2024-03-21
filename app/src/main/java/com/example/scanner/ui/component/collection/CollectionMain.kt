package com.example.scanner.ui.component.collection

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
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.scanner.ui.component.Error
import com.example.scanner.ui.component.Loading
import com.example.scanner.ui.viewmodel.CollectionUiState
import com.example.scanner.ui.viewmodel.CollectionViewModel

@Composable
fun CollectionMain(
    collectionViewModel: CollectionViewModel,
    onVolumeItemClicked: (Int) -> Unit
) {
    val collectionUiState by collectionViewModel.collectionUiState.collectAsState()
    
    LaunchedEffect(Unit) {
        collectionViewModel.getVolumeList()
    }

    when(collectionUiState) {
        CollectionUiState.Error -> Error()
        CollectionUiState.Loading -> Loading()
        is CollectionUiState.Success -> {
            Scaffold(
                topBar = {
                    CollectionTopNavigator(
                        collectionTopSearchBarUiModel =
                        (collectionUiState as CollectionUiState.Success).collectionTopSearchBarUiModel,
                        onSearchBarActiveChanged = {
                            collectionViewModel.switchTopSearchBarActiveState()
                        },
                        onSearchBarInputChanged = { topSearchBarInput ->
                            collectionViewModel.updateTopSearchBarInput(topSearchBarInput)
                        }
                    )
                },
                bottomBar = {
                    CollectionBottomButtonGroup(
                        volumeItemDeleteEnabled =
                        (collectionUiState as CollectionUiState.Success).volumeItemDeleteEnabled,
                        onAddDialogVisibleChanged = {
                            collectionViewModel.switchVolumeAddDialogVisibility()
                        },
                        onDeleteDialogVisibleChanged = {
                            collectionViewModel.switchVolumeDeleteDialogVisibility()
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
                    VolumeDisplayBody(
                        allVolumeItemCheckedState = (collectionUiState as CollectionUiState.Success).allVolumeItemCheckedState,
                        volumeItemUiModelList = (collectionUiState as CollectionUiState.Success).volumeItemUiModelList,
                        onItemClicked = onVolumeItemClicked,
                        onAllItemCheckedStateChanged = { checkedState ->
                            collectionViewModel.switchAllVolumeItemCheckedState(checkedState)
                        },
                        onItemCheckedChanged = { volumeItemUiModel ->
                            collectionViewModel.switchVolumeItemCheckedState(volumeItemUiModel)
                        },
                        onMenuVisibleChanged = { volumeItemUiModel ->
                            collectionViewModel.switchVolumeItemMenuVisibility(volumeItemUiModel)
                        },
                        onEditDialogVisibleChanged = {volumeItemUiModel ->
                            collectionViewModel.switchVolumeEditDialogVisibility(volumeItemUiModel)
                        },
                        onViewDialogVisibleChanged = {volumeItemUiModel ->
                            collectionViewModel.switchVolumeViewDialogVisibility(volumeItemUiModel)
                        }
                    )
                }

                VolumeEditDialog(
                    volumeEditUiModel = (collectionUiState as CollectionUiState.Success).volumeEditUiModel,
                    onDialogVisibleChanged = { volumeItemUiModel ->
                        collectionViewModel.switchVolumeEditDialogVisibility(volumeItemUiModel)
                    },
                    onDialogVolumeNameInputChanged = {inputName ->
                        collectionViewModel.updateEditDialogVolumeNameInput(inputName)
                    },
                    onEditRequestSubmitted = {volumeEditUiModel ->
                        collectionViewModel.submitUpdateVolume(volumeEditUiModel)
                    }
                )

                VolumeAddDialog(
                    volumeAddUiModel = (collectionUiState as CollectionUiState.Success).volumeAddUiModel,
                    onDialogVisibleChanged = {
                        collectionViewModel.switchVolumeAddDialogVisibility()
                    },
                    onDialogVolumeNameInputChanged = {inputName ->
                        collectionViewModel.updateAddDialogVolumeNameInput(inputName)
                    },
                    onAddRequestSubmitted = { volumeAddUiModel ->
                        collectionViewModel.submitAddVolume(volumeAddUiModel)
                    }
                )

                VolumeDeleteDialog(
                    volumeDeleteUiModel = (collectionUiState as CollectionUiState.Success).volumeDeleteUiModel,
                    onDialogVisibleChanged = {
                        collectionViewModel.switchVolumeDeleteDialogVisibility()
                    },
                    onDeleteRequestSubmitted = {
                        collectionViewModel.submitDeleteVolume()
                    }
                )

                VolumeViewDialog(
                    volumeViewUiModel = (collectionUiState as CollectionUiState.Success).volumeViewUiModel,
                    onDialogVisibleChanged = {
                        collectionViewModel.switchVolumeViewDialogVisibility(null)
                    }
                )
            }
        }
    }
}