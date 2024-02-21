package com.example.scanner.ui.viewmodel

import android.app.Activity
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.state.ToggleableState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scanner.data.repo.CollectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProjectViewModel @Inject constructor(
    private val collectionRepository: CollectionRepository
): ViewModel() {
    private val _projectUiState: MutableStateFlow<ProjectUiState> =
        MutableStateFlow(ProjectUiState.Loading)
    val projectUiState: StateFlow<ProjectUiState>
        get() = _projectUiState

    private val _projectId = MutableLiveData<Int>(0)
    val projectId: LiveData<Int> = _projectId

    fun setProjectId(projectId: Int) {
        _projectId.value = projectId
    }

    fun getCollectionList() {
        val projectId = _projectId.value ?: run {
            _projectUiState.value = ProjectUiState.Error
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            collectionRepository.getCollectionByProjectIdAsUiModel(projectId).collect() { collectionList ->
                _projectUiState.value = ProjectUiState.Success(
                    allCollectionItemCheckedState = ToggleableState.Off,
                    collectionItemDeleteEnabled = false,
                    projectTopSearchBarUiModel = ProjectTopSearchBarUiModel(
                        false,
                        ""
                    ),
                    collectionAddUiModel = CollectionAddUiModel(
                        "",
                        false
                    ),
                    collectionEditUiModel = CollectionEditUiModel(
                        0,
                        "",
                        false
                    ),
                    collectionDeleteUiModel = CollectionDeleteUiModel(
                        false
                    ),
                    collectionItemUiModelList = collectionList
                )
            }

        }
    }

    // ----------------------------------------------------------------------------------
    // The following functions are called after _projectUiState.value is ProjectUiState.Success
    // ----------------------------------------------------------------------------------

    fun readExcelFile(uri: Uri?) {
        // TODO: use cpp function to read excel file
        // write data into database
    }

    fun switchCollectionItemCheckedState
                (collectionItemUiModel: CollectionItemUiModel) {

        // remain the models of all other items
        // only change the checked status of the selected item
        // trigger ui update by setting MutableStateFlow.value

        viewModelScope.launch {

            var newListToggleableState: ToggleableState = ToggleableState.Indeterminate
            var itemDeleteEnabled: Boolean = false
            var checkedItemCount: Int = 0

            // update the list of the list item checked state by creating a new list object

            val newItemList = (_projectUiState.value as ProjectUiState.Success)
                .collectionItemUiModelList.map { model ->
                    if (model.collectionId == collectionItemUiModel.collectionId) {
                        if (!model.itemChecked) {
                            checkedItemCount += 1
                            itemDeleteEnabled = true
                        }

                        model.copy(itemChecked = !collectionItemUiModel.itemChecked)
                    } else {
                        if (model.itemChecked) {
                            checkedItemCount += 1
                            itemDeleteEnabled = true
                        }

                        model
                    }
                }

            // synchronize the checkbox state with the number of checked item

            if (checkedItemCount == (_projectUiState.value as ProjectUiState.Success).
                collectionItemUiModelList.size) {
                newListToggleableState = ToggleableState.On
            }

            if (checkedItemCount == 0)
                newListToggleableState = ToggleableState.Off

            _projectUiState.value = ProjectUiState.Success(
                newListToggleableState,
                itemDeleteEnabled,
                (_projectUiState.value as ProjectUiState.Success).projectTopSearchBarUiModel,
                (_projectUiState.value as ProjectUiState.Success).collectionAddUiModel,
                (_projectUiState.value as ProjectUiState.Success).collectionEditUiModel,
                (_projectUiState.value as ProjectUiState.Success).collectionDeleteUiModel,
                newItemList
            )
        }
    }

    fun switchAllCollectionItemCheckedState(allCollectionItemCheckedState: ToggleableState) {
        viewModelScope.launch {

            var allItemChecked: Boolean = false
            var itemDeleteEnabled: Boolean = false
            var newListToggleableState: ToggleableState = ToggleableState.Off

            // click a checkbox of `indeterminate` state will push it to `on` state

            if (allCollectionItemCheckedState == ToggleableState.Indeterminate
                || allCollectionItemCheckedState == ToggleableState.Off) {
                allItemChecked = true
                itemDeleteEnabled = true
                newListToggleableState = ToggleableState.On
            }

            // update the list of the list item checked state by creating a new list object

            val newItemList = (_projectUiState.value as ProjectUiState.Success)
                .collectionItemUiModelList.map { model ->
                    model.copy(itemChecked = allItemChecked)
                }

            _projectUiState.value = ProjectUiState.Success(
                newListToggleableState,
                itemDeleteEnabled,
                (_projectUiState.value as ProjectUiState.Success).projectTopSearchBarUiModel,
                (_projectUiState.value as ProjectUiState.Success).collectionAddUiModel,
                (_projectUiState.value as ProjectUiState.Success).collectionEditUiModel,
                (_projectUiState.value as ProjectUiState.Success).collectionDeleteUiModel,
                newItemList
            )
        }
    }

    fun switchCollectionItemMenuVisibility(collectionItemUiModel: CollectionItemUiModel) {

        // remain the models of all other items
        // only change the menu visibility state of the selected item
        // trigger ui update by setting MutableStateFlow.value

        viewModelScope.launch {

            val newItemList = (_projectUiState.value as ProjectUiState.Success)
                .collectionItemUiModelList.map { model ->
                    if (model.collectionId == collectionItemUiModel.collectionId) {
                        model.copy(menuVisible = !collectionItemUiModel.menuVisible)
                    } else {
                        model
                    }
                }
            _projectUiState.value = ProjectUiState.Success(
                (_projectUiState.value as ProjectUiState.Success).allCollectionItemCheckedState,
                (_projectUiState.value as ProjectUiState.Success).collectionItemDeleteEnabled,
                (_projectUiState.value as ProjectUiState.Success).projectTopSearchBarUiModel,
                (_projectUiState.value as ProjectUiState.Success).collectionAddUiModel,
                (_projectUiState.value as ProjectUiState.Success).collectionEditUiModel,
                (_projectUiState.value as ProjectUiState.Success).collectionDeleteUiModel,
                newItemList
            )
        }
    }

    fun switchTopSearchBarActiveState() {
        viewModelScope.launch {

            val newSearchBarUiModel = ProjectTopSearchBarUiModel (
                !(_projectUiState.value as ProjectUiState.Success).projectTopSearchBarUiModel.activeState,
                (_projectUiState.value as ProjectUiState.Success).projectTopSearchBarUiModel.inputQuery
            )

            _projectUiState.value = ProjectUiState.Success(
                (_projectUiState.value as ProjectUiState.Success).allCollectionItemCheckedState,
                (_projectUiState.value as ProjectUiState.Success).collectionItemDeleteEnabled,
                newSearchBarUiModel,
                (_projectUiState.value as ProjectUiState.Success).collectionAddUiModel,
                (_projectUiState.value as ProjectUiState.Success).collectionEditUiModel,
                (_projectUiState.value as ProjectUiState.Success).collectionDeleteUiModel,
                (_projectUiState.value as ProjectUiState.Success).collectionItemUiModelList
            )
        }
    }

    fun updateTopSearchBarInput(inputQuery: String) {

        // we maintain all the ui state in this ViewModel class
        // so we change the inputQuery here instead of within the TopNavigator component

        viewModelScope.launch {

            val newSearchBarUiModel = ProjectTopSearchBarUiModel (
                (_projectUiState.value as ProjectUiState.Success).projectTopSearchBarUiModel.activeState,
                inputQuery
            )

            _projectUiState.value = ProjectUiState.Success(
                (_projectUiState.value as ProjectUiState.Success).allCollectionItemCheckedState,
                (_projectUiState.value as ProjectUiState.Success).collectionItemDeleteEnabled,
                newSearchBarUiModel,
                (_projectUiState.value as ProjectUiState.Success).collectionAddUiModel,
                (_projectUiState.value as ProjectUiState.Success).collectionEditUiModel,
                (_projectUiState.value as ProjectUiState.Success).collectionDeleteUiModel,
                (_projectUiState.value as ProjectUiState.Success).collectionItemUiModelList
            )
        }
    }

    fun switchCollectionEditDialogVisibility(collectionItemUiModel: CollectionItemUiModel?) {
        viewModelScope.launch {
            if (collectionItemUiModel == null) {

                // called by dialog cancel button
                // clear all the input and close the dialog

                val newEditUiModel = CollectionEditUiModel(
                    collectionId = 0,
                    collectionName = "",
                    dialogVisible = false
                )

                _projectUiState.value = ProjectUiState.Success(
                    (_projectUiState.value as ProjectUiState.Success).allCollectionItemCheckedState,
                    (_projectUiState.value as ProjectUiState.Success).collectionItemDeleteEnabled,
                    (_projectUiState.value as ProjectUiState.Success).projectTopSearchBarUiModel,
                    (_projectUiState.value as ProjectUiState.Success).collectionAddUiModel,
                    newEditUiModel,
                    (_projectUiState.value as ProjectUiState.Success).collectionDeleteUiModel,
                    (_projectUiState.value as ProjectUiState.Success).collectionItemUiModelList
                )
            } else {

                // close the menu list

                switchCollectionItemMenuVisibility(collectionItemUiModel)

                // initialize the dialog default value according to collectionItemUiModel

                val newEditUiModel = CollectionEditUiModel(
                    collectionId = collectionItemUiModel.collectionId,
                    collectionName = collectionItemUiModel.collectionName,
                    dialogVisible = true
                )

                _projectUiState.value = ProjectUiState.Success(
                    (_projectUiState.value as ProjectUiState.Success).allCollectionItemCheckedState,
                    (_projectUiState.value as ProjectUiState.Success).collectionItemDeleteEnabled,
                    (_projectUiState.value as ProjectUiState.Success).projectTopSearchBarUiModel,
                    (_projectUiState.value as ProjectUiState.Success).collectionAddUiModel,
                    newEditUiModel,
                    (_projectUiState.value as ProjectUiState.Success).collectionDeleteUiModel,
                    (_projectUiState.value as ProjectUiState.Success).collectionItemUiModelList
                )
            }
        }
    }

    fun switchCollectionAddDialogVisibility() {
        viewModelScope.launch {

            val currentAddUiModel = (_projectUiState.value as ProjectUiState.Success).collectionAddUiModel

            val newAddUiModel = CollectionAddUiModel(
                "",
                !currentAddUiModel.dialogVisible
            )

            _projectUiState.value = ProjectUiState.Success(
                (_projectUiState.value as ProjectUiState.Success).allCollectionItemCheckedState,
                (_projectUiState.value as ProjectUiState.Success).collectionItemDeleteEnabled,
                (_projectUiState.value as ProjectUiState.Success).projectTopSearchBarUiModel,
                newAddUiModel,
                (_projectUiState.value as ProjectUiState.Success).collectionEditUiModel,
                (_projectUiState.value as ProjectUiState.Success).collectionDeleteUiModel,
                (_projectUiState.value as ProjectUiState.Success).collectionItemUiModelList
            )
        }
    }

    fun switchCollectionDeleteDialogVisibility() {
        viewModelScope.launch {

            val newDeleteUiModel = CollectionDeleteUiModel (
                !(_projectUiState.value as ProjectUiState.Success).collectionDeleteUiModel.dialogVisible
            )

            _projectUiState.value = ProjectUiState.Success(
                (_projectUiState.value as ProjectUiState.Success).allCollectionItemCheckedState,
                (_projectUiState.value as ProjectUiState.Success).collectionItemDeleteEnabled,
                (_projectUiState.value as ProjectUiState.Success).projectTopSearchBarUiModel,
                (_projectUiState.value as ProjectUiState.Success).collectionAddUiModel,
                (_projectUiState.value as ProjectUiState.Success).collectionEditUiModel,
                newDeleteUiModel,
                (_projectUiState.value as ProjectUiState.Success).collectionItemUiModelList
            )
        }
    }

    fun updateAddDialogCollectionNameInput(inputName: String) {
        viewModelScope.launch {

            val newCollectionAddUiModel = CollectionAddUiModel(
                inputName,
                (_projectUiState.value as ProjectUiState.Success).collectionAddUiModel.dialogVisible
            )

            _projectUiState.value = ProjectUiState.Success(
                (_projectUiState.value as ProjectUiState.Success).allCollectionItemCheckedState,
                (_projectUiState.value as ProjectUiState.Success).collectionItemDeleteEnabled,
                (_projectUiState.value as ProjectUiState.Success).projectTopSearchBarUiModel,
                newCollectionAddUiModel,
                (_projectUiState.value as ProjectUiState.Success).collectionEditUiModel,
                (_projectUiState.value as ProjectUiState.Success).collectionDeleteUiModel,
                (_projectUiState.value as ProjectUiState.Success).collectionItemUiModelList
            )
        }
    }

    fun submitAddCollection(collectionAddUiModel: CollectionAddUiModel) {
        val projectId = _projectId.value ?: run {
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            collectionRepository.insertOneCollectionFromUiModel(projectId, collectionAddUiModel)
        }
    }

    fun updateEditDialogCollectionNameInput(inputName: String) {
        viewModelScope.launch {

            val editUiModel = (_projectUiState.value as ProjectUiState.Success).collectionEditUiModel

            val newEditUiModel = CollectionEditUiModel(
                editUiModel.collectionId,
                inputName,
                editUiModel.dialogVisible
            )

            _projectUiState.value = ProjectUiState.Success(
                (_projectUiState.value as ProjectUiState.Success).allCollectionItemCheckedState,
                (_projectUiState.value as ProjectUiState.Success).collectionItemDeleteEnabled,
                (_projectUiState.value as ProjectUiState.Success).projectTopSearchBarUiModel,
                (_projectUiState.value as ProjectUiState.Success).collectionAddUiModel,
                newEditUiModel,
                (_projectUiState.value as ProjectUiState.Success).collectionDeleteUiModel,
                (_projectUiState.value as ProjectUiState.Success).collectionItemUiModelList
            )
        }
    }

    fun submitDeleteCollection() {
        viewModelScope.launch(Dispatchers.IO) {

            collectionRepository.deleteCollectionFromUiModelList(
                (_projectUiState.value as ProjectUiState.Success).collectionItemUiModelList
            )
        }
    }

    fun submitUpdateCollection(collectionEditUiModel: CollectionEditUiModel) {
        viewModelScope.launch(Dispatchers.IO) {

            collectionRepository.updateCollectionFromUiModel(collectionEditUiModel)
        }
    }
}

data class CollectionItemUiModel (
    val collectionId: Int,
    val collectionName: String,
    val modifyTime: String,
    val itemChecked: Boolean,
    val menuVisible: Boolean
)

data class ProjectTopSearchBarUiModel (
    val activeState: Boolean,
    val inputQuery: String
)

data class CollectionAddUiModel (
    val collectionName: String,
    val dialogVisible: Boolean
)

data class CollectionEditUiModel (
    val collectionId: Int,
    val collectionName: String,
    val dialogVisible: Boolean
)

data class CollectionDeleteUiModel (
    val dialogVisible: Boolean
)

sealed class ProjectUiState {
    data class Success (
        val allCollectionItemCheckedState: ToggleableState,
        val collectionItemDeleteEnabled: Boolean,
        val projectTopSearchBarUiModel: ProjectTopSearchBarUiModel,
        val collectionAddUiModel: CollectionAddUiModel,
        val collectionEditUiModel: CollectionEditUiModel,
        val collectionDeleteUiModel: CollectionDeleteUiModel,
        val collectionItemUiModelList: List<CollectionItemUiModel>
    ): ProjectUiState()
    data object Loading: ProjectUiState()
    data object Error: ProjectUiState()
}