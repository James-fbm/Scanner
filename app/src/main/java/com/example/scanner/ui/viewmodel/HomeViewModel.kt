package com.example.scanner.ui.viewmodel

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
                projectRepository.getAllProject().mapIndexed {index, entity ->
                    ProjectListItemUiModel(
                        id = index,
                        projectName = entity.projectName,
                        modifyTime = entity.modifyTime,
                        itemChecked = false,
                        menuVisible = false
                    )
                }
            )
        }
    }

    fun changeProjectItemCheckedStatus
                (projectListItemUiModel: ProjectListItemUiModel) {
        // remain the models of all other items
        // only change the checked status of the selected item
        // trigger ui update by setting MutableStateFlow.value

        viewModelScope.launch {
            when(homeUiState.value) {
                HomeUiState.Error -> {}
                HomeUiState.Loading -> {}
                is HomeUiState.Success -> {
                    val isChecked = (_homeUiState.value as HomeUiState.Success)
                        .projectListItemUiModelList[projectListItemUiModel.id].itemChecked

                    // update the list of the list item checked state by creating a new list object

                    val newItemList = (_homeUiState.value as HomeUiState.Success)
                        .projectListItemUiModelList.map { model ->
                            if (model.id == projectListItemUiModel.id) {
                                model.copy(itemChecked = !isChecked)
                            } else {
                                model
                            }
                        }
                    _homeUiState.value = HomeUiState.Success(newItemList)
                }
            }
        }
    }

    fun changeProjectItemMenuVisibility(projectListItemUiModel: ProjectListItemUiModel) {
        // remain the models of all other items
        // only change the menu visibility state of the selected item
        // trigger ui update by setting MutableStateFlow.value

        viewModelScope.launch {
            when (homeUiState.value) {
                HomeUiState.Error -> {}
                HomeUiState.Loading -> {}
                is HomeUiState.Success -> {
                    val menuVisible = (_homeUiState.value as HomeUiState.Success)
                        .projectListItemUiModelList[projectListItemUiModel.id].menuVisible

                    // update the list of the list item checked state by creating a new list object

                    val newItemList = (_homeUiState.value as HomeUiState.Success)
                        .projectListItemUiModelList.map { model ->
                            if (model.id == projectListItemUiModel.id) {
                                model.copy(menuVisible = !menuVisible)
                            } else {
                                model
                            }
                        }
                    _homeUiState.value = HomeUiState.Success(newItemList)
                }
            }
        }
    }
}

data class ProjectListItemUiModel (
    val id: Int,
    val projectName: String,
    val modifyTime: String,
    val itemChecked: Boolean,
    val menuVisible: Boolean
)

sealed class HomeUiState {
    data class Success (
        val projectListItemUiModelList: List<ProjectListItemUiModel>
    ): HomeUiState()
    data object Loading: HomeUiState()
    data object Error: HomeUiState()
}