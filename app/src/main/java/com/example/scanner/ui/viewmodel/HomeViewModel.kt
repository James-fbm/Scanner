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
                allProjectListItemCheckedState = ToggleableState.Off,
                enableProjectListItemDelete = false,
                topSearchBarInput = "",
                projectListItemUiModelList = projectRepository.getAllProject().mapIndexed {index, entity ->
                    ProjectListItemUiModel(
                        sequenceId = index,
                        projectName = entity.projectName,
                        modifyTime = entity.modifyTime,
                        itemChecked = false,
                        menuVisible = false
                    )
                }
            )
        }
    }

    fun switchProjectListItemCheckedState
                (projectListItemUiModel: ProjectListItemUiModel) {

        // remain the models of all other items
        // only change the checked status of the selected item
        // trigger ui update by setting MutableStateFlow.value

        viewModelScope.launch {
            when(homeUiState.value) {
                HomeUiState.Error -> {}
                HomeUiState.Loading -> {}
                is HomeUiState.Success -> {

                    var newListToggleableState: ToggleableState = ToggleableState.Indeterminate
                    var enableItemDelete: Boolean = false
                    var checkedItemCount: Int = 0

                    // update the list of the list item checked state by creating a new list object

                    val newItemList = (_homeUiState.value as HomeUiState.Success)
                        .projectListItemUiModelList.map { model ->
                            if (model.sequenceId == projectListItemUiModel.sequenceId) {
                                if (!model.itemChecked) {
                                    checkedItemCount += 1
                                    enableItemDelete = true
                                }

                                model.copy(itemChecked = !projectListItemUiModel.itemChecked)
                            } else {
                                if (model.itemChecked) {
                                    checkedItemCount += 1
                                    enableItemDelete = true
                                }

                                model
                            }
                        }

                    // synchronize the checkbox state with the number of checked item

                    if (checkedItemCount == (_homeUiState.value as HomeUiState.Success).
                        projectListItemUiModelList.size) {
                        newListToggleableState = ToggleableState.On
                    }

                    if (checkedItemCount == 0)
                        newListToggleableState = ToggleableState.Off

                    _homeUiState.value = HomeUiState.Success(
                        newListToggleableState,
                        enableItemDelete,
                        (_homeUiState.value as HomeUiState.Success).topSearchBarInput,
                        newItemList
                    )
                }
            }
        }
    }

    fun switchAllProjectListItemCheckedState(allProjectListItemCheckedState: ToggleableState) {
        viewModelScope.launch {
            when(homeUiState.value) {
                HomeUiState.Error -> {}
                HomeUiState.Loading -> {}
                is HomeUiState.Success -> {

                    var allItemChecked: Boolean = false
                    var enableItemDelete: Boolean = false
                    var newListToggleableState: ToggleableState = ToggleableState.Off

                    // click a checkbox of `indeterminate` state will push it to `on` state

                    if (allProjectListItemCheckedState == ToggleableState.Indeterminate
                        || allProjectListItemCheckedState == ToggleableState.Off) {
                        allItemChecked = true
                        enableItemDelete = true
                        newListToggleableState = ToggleableState.On
                    }

                    // update the list of the list item checked state by creating a new list object

                    val newItemList = (_homeUiState.value as HomeUiState.Success)
                        .projectListItemUiModelList.map { model ->
                                model.copy(itemChecked = allItemChecked)
                        }

                    _homeUiState.value = HomeUiState.Success(
                        newListToggleableState,
                        enableItemDelete,
                        (_homeUiState.value as HomeUiState.Success).topSearchBarInput,
                        newItemList
                    )
                }
            }
        }
    }

    fun switchProjectListItemMenuVisibility(projectListItemUiModel: ProjectListItemUiModel) {

        // remain the models of all other items
        // only change the menu visibility state of the selected item
        // trigger ui update by setting MutableStateFlow.value

        viewModelScope.launch {
            when (homeUiState.value) {
                HomeUiState.Error -> {}
                HomeUiState.Loading -> {}
                is HomeUiState.Success -> {

                    // update the list of the list item checked state by creating a new list object

                    val newItemList = (_homeUiState.value as HomeUiState.Success)
                        .projectListItemUiModelList.map { model ->
                            if (model.sequenceId == projectListItemUiModel.sequenceId) {
                                model.copy(menuVisible = !projectListItemUiModel.menuVisible)
                            } else {
                                model
                            }
                        }
                    _homeUiState.value = HomeUiState.Success(
                        (_homeUiState.value as HomeUiState.Success).allProjectListItemCheckedState,
                        (_homeUiState.value as HomeUiState.Success).enableProjectListItemDelete,
                        (_homeUiState.value as HomeUiState.Success).topSearchBarInput,
                        newItemList
                    )
                }
            }
        }
    }

    fun updateTopSearchBarInput(topSearchBarInput: String) {
        viewModelScope.launch {
            when (homeUiState.value) {
                HomeUiState.Error -> {}
                HomeUiState.Loading -> {}
                is HomeUiState.Success -> {
                    _homeUiState.value = HomeUiState.Success(
                        (_homeUiState.value as HomeUiState.Success).allProjectListItemCheckedState,
                        (_homeUiState.value as HomeUiState.Success).enableProjectListItemDelete,
                        topSearchBarInput,
                        (_homeUiState.value as HomeUiState.Success).projectListItemUiModelList
                    )
                }
            }
        }
    }
}

data class ProjectListItemUiModel (
    val sequenceId: Int,
    val projectName: String,
    val modifyTime: String,
    val itemChecked: Boolean,
    val menuVisible: Boolean
)

sealed class HomeUiState {
    data class Success (
        val allProjectListItemCheckedState: ToggleableState,
        val enableProjectListItemDelete: Boolean,
        val topSearchBarInput: String,
        val projectListItemUiModelList: List<ProjectListItemUiModel>
    ): HomeUiState()
    data object Loading: HomeUiState()
    data object Error: HomeUiState()
}