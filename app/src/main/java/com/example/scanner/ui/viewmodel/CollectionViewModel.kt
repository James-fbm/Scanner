package com.example.scanner.ui.viewmodel

import com.example.scanner.data.repo.VolumeRepository
import androidx.compose.ui.state.ToggleableState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(
    private val volumeRepository: VolumeRepository
): ViewModel() {
    private val _collectionUiState: MutableStateFlow<CollectionUiState> =
        MutableStateFlow(CollectionUiState.Loading)
    val collectionUiState: StateFlow<CollectionUiState>
        get() = _collectionUiState

    private val _collectionId = MutableLiveData<Int>(0)
    val collectionId: LiveData<Int> = _collectionId

    fun setCollectionId(collectionId: Int) {
        _collectionId.value = collectionId
    }

    fun getVolumeList() {
        val collectionId = collectionId.value ?: run {
            _collectionUiState.value = CollectionUiState.Error
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            volumeRepository.getAllVolumeByCollectionIdAsUiModel(collectionId).collect { volumeList ->
                _collectionUiState.value = CollectionUiState.Success(
                    allVolumeItemCheckedState = ToggleableState.Off,
                    volumeItemDeleteEnabled = false,
                    collectionTopSearchBarUiModel = CollectionTopSearchBarUiModel(
                        false,
                        ""
                    ),
                    volumeAddUiModel = VolumeAddUiModel(
                        "",
                        false
                    ),
                    volumeEditUiModel = VolumeEditUiModel(
                        0,
                        "",
                        false
                    ),
                    volumeDeleteUiModel = VolumeDeleteUiModel(
                        false
                    ),
                    volumeItemUiModelList = volumeList
                )
            }

        }
    }

    // ----------------------------------------------------------------------------------
    // The following functions are called after _collectionUiState.value is CollectionUiState.Success
    // ----------------------------------------------------------------------------------

    fun switchVolumeItemCheckedState
                (volumeItemUiModel: VolumeItemUiModel) {

        // remain the models of all other items
        // only change the checked status of the selected item
        // trigger ui update by setting MutableStateFlow.value

        viewModelScope.launch {

            var newListToggleableState: ToggleableState = ToggleableState.Indeterminate
            var itemDeleteEnabled: Boolean = false
            var checkedItemCount: Int = 0

            // update the list of the list item checked state by creating a new list object

            val newItemList = (_collectionUiState.value as CollectionUiState.Success)
                .volumeItemUiModelList.map { model ->
                    if (model.volumeId == volumeItemUiModel.volumeId) {
                        if (!model.itemChecked) {
                            checkedItemCount += 1
                            itemDeleteEnabled = true
                        }

                        model.copy(itemChecked = !volumeItemUiModel.itemChecked)
                    } else {
                        if (model.itemChecked) {
                            checkedItemCount += 1
                            itemDeleteEnabled = true
                        }

                        model
                    }
                }

            // synchronize the checkbox state with the number of checked item

            if (checkedItemCount == (_collectionUiState.value as CollectionUiState.Success).
                volumeItemUiModelList.size) {
                newListToggleableState = ToggleableState.On
            }

            if (checkedItemCount == 0)
                newListToggleableState = ToggleableState.Off

            _collectionUiState.value = CollectionUiState.Success(
                newListToggleableState,
                itemDeleteEnabled,
                (_collectionUiState.value as CollectionUiState.Success).collectionTopSearchBarUiModel,
                (_collectionUiState.value as CollectionUiState.Success).volumeAddUiModel,
                (_collectionUiState.value as CollectionUiState.Success).volumeEditUiModel,
                (_collectionUiState.value as CollectionUiState.Success).volumeDeleteUiModel,
                newItemList
            )
        }
    }

    fun switchAllVolumeItemCheckedState(allVolumeItemCheckedState: ToggleableState) {
        viewModelScope.launch {

            var allItemChecked: Boolean = false
            var itemDeleteEnabled: Boolean = false
            var newListToggleableState: ToggleableState = ToggleableState.Off

            // click a checkbox of `indeterminate` state will push it to `on` state

            if (allVolumeItemCheckedState == ToggleableState.Indeterminate
                || allVolumeItemCheckedState == ToggleableState.Off) {
                allItemChecked = true
                itemDeleteEnabled = true
                newListToggleableState = ToggleableState.On
            }

            // update the list of the list item checked state by creating a new list object

            val newItemList = (_collectionUiState.value as CollectionUiState.Success)
                .volumeItemUiModelList.map { model ->
                    model.copy(itemChecked = allItemChecked)
                }

            _collectionUiState.value = CollectionUiState.Success(
                newListToggleableState,
                itemDeleteEnabled,
                (_collectionUiState.value as CollectionUiState.Success).collectionTopSearchBarUiModel,
                (_collectionUiState.value as CollectionUiState.Success).volumeAddUiModel,
                (_collectionUiState.value as CollectionUiState.Success).volumeEditUiModel,
                (_collectionUiState.value as CollectionUiState.Success).volumeDeleteUiModel,
                newItemList
            )
        }
    }

    fun switchVolumeItemMenuVisibility(volumeItemUiModel: VolumeItemUiModel) {

        // remain the models of all other items
        // only change the menu visibility state of the selected item
        // trigger ui update by setting MutableStateFlow.value

        viewModelScope.launch {

            val newItemList = (_collectionUiState.value as CollectionUiState.Success)
                .volumeItemUiModelList.map { model ->
                    if (model.volumeId == volumeItemUiModel.volumeId) {
                        model.copy(menuVisible = !volumeItemUiModel.menuVisible)
                    } else {
                        model
                    }
                }
            _collectionUiState.value = CollectionUiState.Success(
                (_collectionUiState.value as CollectionUiState.Success).allVolumeItemCheckedState,
                (_collectionUiState.value as CollectionUiState.Success).volumeItemDeleteEnabled,
                (_collectionUiState.value as CollectionUiState.Success).collectionTopSearchBarUiModel,
                (_collectionUiState.value as CollectionUiState.Success).volumeAddUiModel,
                (_collectionUiState.value as CollectionUiState.Success).volumeEditUiModel,
                (_collectionUiState.value as CollectionUiState.Success).volumeDeleteUiModel,
                newItemList
            )
        }
    }

    fun switchTopSearchBarActiveState() {
        viewModelScope.launch {

            val newSearchBarUiModel = CollectionTopSearchBarUiModel (
                !(_collectionUiState.value as CollectionUiState.Success).collectionTopSearchBarUiModel.activeState,
                (_collectionUiState.value as CollectionUiState.Success).collectionTopSearchBarUiModel.inputQuery
            )

            _collectionUiState.value = CollectionUiState.Success(
                (_collectionUiState.value as CollectionUiState.Success).allVolumeItemCheckedState,
                (_collectionUiState.value as CollectionUiState.Success).volumeItemDeleteEnabled,
                newSearchBarUiModel,
                (_collectionUiState.value as CollectionUiState.Success).volumeAddUiModel,
                (_collectionUiState.value as CollectionUiState.Success).volumeEditUiModel,
                (_collectionUiState.value as CollectionUiState.Success).volumeDeleteUiModel,
                (_collectionUiState.value as CollectionUiState.Success).volumeItemUiModelList
            )
        }
    }

    fun updateTopSearchBarInput(inputQuery: String) {

        // we maintain all the ui state in this ViewModel class
        // so we change the inputQuery here instead of within the TopNavigator component

        viewModelScope.launch {

            val newSearchBarUiModel = CollectionTopSearchBarUiModel (
                (_collectionUiState.value as CollectionUiState.Success).collectionTopSearchBarUiModel.activeState,
                inputQuery
            )

            _collectionUiState.value = CollectionUiState.Success(
                (_collectionUiState.value as CollectionUiState.Success).allVolumeItemCheckedState,
                (_collectionUiState.value as CollectionUiState.Success).volumeItemDeleteEnabled,
                newSearchBarUiModel,
                (_collectionUiState.value as CollectionUiState.Success).volumeAddUiModel,
                (_collectionUiState.value as CollectionUiState.Success).volumeEditUiModel,
                (_collectionUiState.value as CollectionUiState.Success).volumeDeleteUiModel,
                (_collectionUiState.value as CollectionUiState.Success).volumeItemUiModelList
            )
        }
    }

    fun switchVolumeEditDialogVisibility(volumeItemUiModel: VolumeItemUiModel?) {
        viewModelScope.launch {
            if (volumeItemUiModel == null) {

                // called by dialog cancel button
                // clear all the input and close the dialog

                val newEditUiModel = VolumeEditUiModel(
                    volumeId = 0,
                    volumeName = "",
                    dialogVisible = false
                )

                _collectionUiState.value = CollectionUiState.Success(
                    (_collectionUiState.value as CollectionUiState.Success).allVolumeItemCheckedState,
                    (_collectionUiState.value as CollectionUiState.Success).volumeItemDeleteEnabled,
                    (_collectionUiState.value as CollectionUiState.Success).collectionTopSearchBarUiModel,
                    (_collectionUiState.value as CollectionUiState.Success).volumeAddUiModel,
                    newEditUiModel,
                    (_collectionUiState.value as CollectionUiState.Success).volumeDeleteUiModel,
                    (_collectionUiState.value as CollectionUiState.Success).volumeItemUiModelList
                )
            } else {

                // close the menu list

                switchVolumeItemMenuVisibility(volumeItemUiModel)

                // initialize the dialog default value according to volumeItemUiModel

                val newEditUiModel = VolumeEditUiModel(
                    volumeId = volumeItemUiModel.volumeId,
                    volumeName = volumeItemUiModel.volumeName,
                    dialogVisible = true
                )

                _collectionUiState.value = CollectionUiState.Success(
                    (_collectionUiState.value as CollectionUiState.Success).allVolumeItemCheckedState,
                    (_collectionUiState.value as CollectionUiState.Success).volumeItemDeleteEnabled,
                    (_collectionUiState.value as CollectionUiState.Success).collectionTopSearchBarUiModel,
                    (_collectionUiState.value as CollectionUiState.Success).volumeAddUiModel,
                    newEditUiModel,
                    (_collectionUiState.value as CollectionUiState.Success).volumeDeleteUiModel,
                    (_collectionUiState.value as CollectionUiState.Success).volumeItemUiModelList
                )
            }
        }
    }

    fun switchVolumeAddDialogVisibility() {
        viewModelScope.launch {

            val currentAddUiModel = (_collectionUiState.value as CollectionUiState.Success).volumeAddUiModel

            val newAddUiModel = VolumeAddUiModel(
                "",
                !currentAddUiModel.dialogVisible
            )

            _collectionUiState.value = CollectionUiState.Success(
                (_collectionUiState.value as CollectionUiState.Success).allVolumeItemCheckedState,
                (_collectionUiState.value as CollectionUiState.Success).volumeItemDeleteEnabled,
                (_collectionUiState.value as CollectionUiState.Success).collectionTopSearchBarUiModel,
                newAddUiModel,
                (_collectionUiState.value as CollectionUiState.Success).volumeEditUiModel,
                (_collectionUiState.value as CollectionUiState.Success).volumeDeleteUiModel,
                (_collectionUiState.value as CollectionUiState.Success).volumeItemUiModelList
            )
        }
    }

    fun switchVolumeDeleteDialogVisibility() {
        viewModelScope.launch {

            val newDeleteUiModel = VolumeDeleteUiModel (
                !(_collectionUiState.value as CollectionUiState.Success).volumeDeleteUiModel.dialogVisible
            )

            _collectionUiState.value = CollectionUiState.Success(
                (_collectionUiState.value as CollectionUiState.Success).allVolumeItemCheckedState,
                (_collectionUiState.value as CollectionUiState.Success).volumeItemDeleteEnabled,
                (_collectionUiState.value as CollectionUiState.Success).collectionTopSearchBarUiModel,
                (_collectionUiState.value as CollectionUiState.Success).volumeAddUiModel,
                (_collectionUiState.value as CollectionUiState.Success).volumeEditUiModel,
                newDeleteUiModel,
                (_collectionUiState.value as CollectionUiState.Success).volumeItemUiModelList
            )
        }
    }

    fun updateAddDialogVolumeNameInput(inputName: String) {
        viewModelScope.launch {

            val newVolumeAddUiModel = VolumeAddUiModel(
                inputName,
                (_collectionUiState.value as CollectionUiState.Success).volumeAddUiModel.dialogVisible
            )

            _collectionUiState.value = CollectionUiState.Success(
                (_collectionUiState.value as CollectionUiState.Success).allVolumeItemCheckedState,
                (_collectionUiState.value as CollectionUiState.Success).volumeItemDeleteEnabled,
                (_collectionUiState.value as CollectionUiState.Success).collectionTopSearchBarUiModel,
                newVolumeAddUiModel,
                (_collectionUiState.value as CollectionUiState.Success).volumeEditUiModel,
                (_collectionUiState.value as CollectionUiState.Success).volumeDeleteUiModel,
                (_collectionUiState.value as CollectionUiState.Success).volumeItemUiModelList
            )
        }
    }

    fun submitAddVolume(volumeAddUiModel: VolumeAddUiModel) {
        val collectionId = collectionId.value ?: run {
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            volumeRepository.insertOneVolumeFromUiModel(collectionId, volumeAddUiModel)
        }
    }

    fun updateEditDialogVolumeNameInput(inputName: String) {
        viewModelScope.launch {

            val editUiModel = (_collectionUiState.value as CollectionUiState.Success).volumeEditUiModel

            val newEditUiModel = VolumeEditUiModel(
                editUiModel.volumeId,
                inputName,
                editUiModel.dialogVisible
            )

            _collectionUiState.value = CollectionUiState.Success(
                (_collectionUiState.value as CollectionUiState.Success).allVolumeItemCheckedState,
                (_collectionUiState.value as CollectionUiState.Success).volumeItemDeleteEnabled,
                (_collectionUiState.value as CollectionUiState.Success).collectionTopSearchBarUiModel,
                (_collectionUiState.value as CollectionUiState.Success).volumeAddUiModel,
                newEditUiModel,
                (_collectionUiState.value as CollectionUiState.Success).volumeDeleteUiModel,
                (_collectionUiState.value as CollectionUiState.Success).volumeItemUiModelList
            )
        }
    }

    fun submitDeleteVolume() {
        val volumeItemUiModelList = (_collectionUiState.value as CollectionUiState.Success).volumeItemUiModelList
        _collectionUiState.value = CollectionUiState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            volumeRepository.deleteVolumeFromUiModelList(
                volumeItemUiModelList
            )
        }
    }

    fun submitUpdateVolume(volumeEditUiModel: VolumeEditUiModel) {
        viewModelScope.launch(Dispatchers.IO) {

            volumeRepository.updateVolumeFromUiModel(volumeEditUiModel)
        }
    }
}

data class VolumeItemUiModel (
    val volumeId: Int,
    val volumeName: String,
    val modifyTime: String,
    val itemChecked: Boolean,
    val menuVisible: Boolean
)

data class CollectionTopSearchBarUiModel (
    val activeState: Boolean,
    val inputQuery: String
)

data class VolumeAddUiModel (
    val volumeName: String,
    val dialogVisible: Boolean
)

data class VolumeEditUiModel (
    val volumeId: Int,
    val volumeName: String,
    val dialogVisible: Boolean
)

data class VolumeDeleteUiModel (
    val dialogVisible: Boolean
)

sealed class CollectionUiState {
    data class Success (
        val allVolumeItemCheckedState: ToggleableState,
        val volumeItemDeleteEnabled: Boolean,
        val collectionTopSearchBarUiModel: CollectionTopSearchBarUiModel,
        val volumeAddUiModel: VolumeAddUiModel,
        val volumeEditUiModel: VolumeEditUiModel,
        val volumeDeleteUiModel: VolumeDeleteUiModel,
        val volumeItemUiModelList: List<VolumeItemUiModel>
    ): CollectionUiState()
    data object Loading: CollectionUiState()
    data object Error: CollectionUiState()
}