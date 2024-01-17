package com.example.scanner.ui.viewmodel

import androidx.compose.ui.state.ToggleableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scanner.data.repo.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val projectRepository: ProjectRepository
): ViewModel() {
    private val _homeUiState: MutableStateFlow<HomeUiState> =
        MutableStateFlow(HomeUiState.Loading)
    val homeUiState: StateFlow<HomeUiState>
        get() = _homeUiState

    fun getProjectList() {
        viewModelScope.launch(Dispatchers.IO) {
            _homeUiState.value = HomeUiState.Success(
                allProjectItemCheckedState = ToggleableState.Off,
                projectItemDeleteEnabled = false,
                topSearchBarUiModel = TopSearchBarUiModel(
                    false,
                    ""
                ),
                projectAddUiModel = ProjectAddUiModel(
                    "",
                    false
                ),
                projectEditUiModel = ProjectEditUiModel(
                    0,
                    "",
                    false
                ),
                projectItemUiModelList = projectRepository.getExampleProjectList().mapIndexed { index, entity ->
                    ProjectItemUiModel(
                        sequenceId = index,
                        projectName = entity.projectName,
                        modifyTime = entity.modifyTime,
                        itemChecked = false,
                        menuVisible = false,
                    )
                }
            )
        }
    }

    // ----------------------------------------------------------------------------------
    // The following functions are called after _homeUiState.value is HomeUiState.Success
    // ----------------------------------------------------------------------------------

    fun switchProjectItemCheckedState
                (projectItemUiModel: ProjectItemUiModel) {

        // remain the models of all other items
        // only change the checked status of the selected item
        // trigger ui update by setting MutableStateFlow.value

        viewModelScope.launch {

            var newListToggleableState: ToggleableState = ToggleableState.Indeterminate
            var itemDeleteEnabled: Boolean = false
            var checkedItemCount: Int = 0

            // update the list of the list item checked state by creating a new list object

            val newItemList = (_homeUiState.value as HomeUiState.Success)
                .projectItemUiModelList.map { model ->
                    if (model.sequenceId == projectItemUiModel.sequenceId) {
                        if (!model.itemChecked) {
                            checkedItemCount += 1
                            itemDeleteEnabled = true
                        }

                        model.copy(itemChecked = !projectItemUiModel.itemChecked)
                    } else {
                        if (model.itemChecked) {
                            checkedItemCount += 1
                            itemDeleteEnabled = true
                        }

                        model
                    }
                }

            // synchronize the checkbox state with the number of checked item

            if (checkedItemCount == (_homeUiState.value as HomeUiState.Success).
                projectItemUiModelList.size) {
                newListToggleableState = ToggleableState.On
            }

            if (checkedItemCount == 0)
                newListToggleableState = ToggleableState.Off

            _homeUiState.value = HomeUiState.Success(
                newListToggleableState,
                itemDeleteEnabled,
                (_homeUiState.value as HomeUiState.Success).topSearchBarUiModel,
                (_homeUiState.value as HomeUiState.Success).projectAddUiModel,
                (_homeUiState.value as HomeUiState.Success).projectEditUiModel,
                newItemList
            )
        }
    }

    fun switchAllProjectItemCheckedState(allProjectItemCheckedState: ToggleableState) {
        viewModelScope.launch {

            var allItemChecked: Boolean = false
            var itemDeleteEnabled: Boolean = false
            var newListToggleableState: ToggleableState = ToggleableState.Off

            // click a checkbox of `indeterminate` state will push it to `on` state

            if (allProjectItemCheckedState == ToggleableState.Indeterminate
                || allProjectItemCheckedState == ToggleableState.Off) {
                allItemChecked = true
                itemDeleteEnabled = true
                newListToggleableState = ToggleableState.On
            }

            // update the list of the list item checked state by creating a new list object

            val newItemList = (_homeUiState.value as HomeUiState.Success)
                .projectItemUiModelList.map { model ->
                        model.copy(itemChecked = allItemChecked)
                }

            _homeUiState.value = HomeUiState.Success(
                newListToggleableState,
                itemDeleteEnabled,
                (_homeUiState.value as HomeUiState.Success).topSearchBarUiModel,
                (_homeUiState.value as HomeUiState.Success).projectAddUiModel,
                (_homeUiState.value as HomeUiState.Success).projectEditUiModel,
                newItemList
            )
        }
    }

    fun switchProjectItemMenuVisibility(projectItemUiModel: ProjectItemUiModel) {

        // remain the models of all other items
        // only change the menu visibility state of the selected item
        // trigger ui update by setting MutableStateFlow.value

        viewModelScope.launch {

            val newItemList = (_homeUiState.value as HomeUiState.Success)
                .projectItemUiModelList.map { model ->
                    if (model.sequenceId == projectItemUiModel.sequenceId) {
                        model.copy(menuVisible = !projectItemUiModel.menuVisible)
                    } else {
                        model
                    }
                }
            _homeUiState.value = HomeUiState.Success(
                (_homeUiState.value as HomeUiState.Success).allProjectItemCheckedState,
                (_homeUiState.value as HomeUiState.Success).projectItemDeleteEnabled,
                (_homeUiState.value as HomeUiState.Success).topSearchBarUiModel,
                (_homeUiState.value as HomeUiState.Success).projectAddUiModel,
                (_homeUiState.value as HomeUiState.Success).projectEditUiModel,
                newItemList
            )
        }
    }

    fun switchTopSearchBarActiveState() {
        viewModelScope.launch {

            val newSearchBarUiModel = TopSearchBarUiModel (
                !(_homeUiState.value as HomeUiState.Success).topSearchBarUiModel.activeState,
                (_homeUiState.value as HomeUiState.Success).topSearchBarUiModel.inputQuery
            )

            _homeUiState.value = HomeUiState.Success(
                (_homeUiState.value as HomeUiState.Success).allProjectItemCheckedState,
                (_homeUiState.value as HomeUiState.Success).projectItemDeleteEnabled,
                newSearchBarUiModel,
                (_homeUiState.value as HomeUiState.Success).projectAddUiModel,
                (_homeUiState.value as HomeUiState.Success).projectEditUiModel,
                (_homeUiState.value as HomeUiState.Success).projectItemUiModelList
            )
        }
    }

    fun updateTopSearchBarInput(inputQuery: String) {

        // we maintain all the ui state in this ViewModel class
        // so we change the inputQuery here instead of within the TopNavigator component

        viewModelScope.launch {

            val newSearchBarUiModel = TopSearchBarUiModel (
                (_homeUiState.value as HomeUiState.Success).topSearchBarUiModel.activeState,
                inputQuery
            )

            _homeUiState.value = HomeUiState.Success(
                (_homeUiState.value as HomeUiState.Success).allProjectItemCheckedState,
                (_homeUiState.value as HomeUiState.Success).projectItemDeleteEnabled,
                newSearchBarUiModel,
                (_homeUiState.value as HomeUiState.Success).projectAddUiModel,
                (_homeUiState.value as HomeUiState.Success).projectEditUiModel,
                (_homeUiState.value as HomeUiState.Success).projectItemUiModelList
            )
        }
    }

    fun switchProjectEditDialogVisibility(projectItemUiModel: ProjectItemUiModel?) {
        viewModelScope.launch {
            if (projectItemUiModel == null) {

                // called by dialog cancel button
                // clear all the input and close the dialog

                val newEditUiModel = ProjectEditUiModel(
                    sequenceId = 0,
                    projectName = "",
                    dialogVisible = false
                )

                _homeUiState.value = HomeUiState.Success(
                    (_homeUiState.value as HomeUiState.Success).allProjectItemCheckedState,
                    (_homeUiState.value as HomeUiState.Success).projectItemDeleteEnabled,
                    (_homeUiState.value as HomeUiState.Success).topSearchBarUiModel,
                    (_homeUiState.value as HomeUiState.Success).projectAddUiModel,
                    newEditUiModel,
                    (_homeUiState.value as HomeUiState.Success).projectItemUiModelList
                )
            } else {

                // close the menu list

                switchProjectItemMenuVisibility(projectItemUiModel)

                // initialize the dialog default value according to projectItemUiModel

                val newEditUiModel = ProjectEditUiModel(
                    sequenceId = projectItemUiModel.sequenceId,
                    projectName = projectItemUiModel.projectName,
                    dialogVisible = true
                )

                _homeUiState.value = HomeUiState.Success(
                    (_homeUiState.value as HomeUiState.Success).allProjectItemCheckedState,
                    (_homeUiState.value as HomeUiState.Success).projectItemDeleteEnabled,
                    (_homeUiState.value as HomeUiState.Success).topSearchBarUiModel,
                    (_homeUiState.value as HomeUiState.Success).projectAddUiModel,
                    newEditUiModel,
                    (_homeUiState.value as HomeUiState.Success).projectItemUiModelList
                )
            }
        }
    }

    fun switchProjectAddDialogVisibility() {
        viewModelScope.launch {

            val currentAddUiModel = (_homeUiState.value as HomeUiState.Success).projectAddUiModel

            val newAddUiModel = ProjectAddUiModel(
                "",
                !currentAddUiModel.dialogVisible
            )

            _homeUiState.value = HomeUiState.Success(
                (_homeUiState.value as HomeUiState.Success).allProjectItemCheckedState,
                (_homeUiState.value as HomeUiState.Success).projectItemDeleteEnabled,
                (_homeUiState.value as HomeUiState.Success).topSearchBarUiModel,
                newAddUiModel,
                (_homeUiState.value as HomeUiState.Success).projectEditUiModel,
                (_homeUiState.value as HomeUiState.Success).projectItemUiModelList
            )
        }
    }
}

data class ProjectItemUiModel (
    val sequenceId: Int,
    val projectName: String,
    val modifyTime: String,
    val itemChecked: Boolean,
    val menuVisible: Boolean
)

data class TopSearchBarUiModel (
    val activeState: Boolean,
    val inputQuery: String
)

data class ProjectAddUiModel (
    val projectName: String,
    val dialogVisible: Boolean
)

data class ProjectEditUiModel (
    val sequenceId: Int,
    val projectName: String,
    val dialogVisible: Boolean
)

sealed class HomeUiState {
    data class Success (
        val allProjectItemCheckedState: ToggleableState,
        val projectItemDeleteEnabled: Boolean,
        val topSearchBarUiModel: TopSearchBarUiModel,
        val projectAddUiModel: ProjectAddUiModel,
        val projectEditUiModel: ProjectEditUiModel,
        val projectItemUiModelList: List<ProjectItemUiModel>
    ): HomeUiState()
    data object Loading: HomeUiState()
    data object Error: HomeUiState()
}