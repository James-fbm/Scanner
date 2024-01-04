package com.example.scanner.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scanner.data.repo.ProjectRepository
import com.example.scanner.ui.component.home.ProjectList
import dagger.hilt.android.lifecycle.HiltViewModel
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
        viewModelScope.launch {
            _homeUiState.value = HomeUiState.Success(
                projectRepository.getAllProject().mapIndexed {index, entity ->
                    ProjectListItemUiModel(
                        id = index,
                        projectName = entity.projectName,
                        modifyTime = entity.modifyTime,
                        isChecked = false
                    )
                }
            )
        }
    }

    fun changeProjectItemCheckedStatus
                (projectListItemUiModel: ProjectListItemUiModel) {
        viewModelScope.launch {
            when(homeUiState.value) {
                HomeUiState.Error -> {}
                HomeUiState.Loading -> {}
                is HomeUiState.Success -> {
                    val isChecked = (_homeUiState.value as HomeUiState.Success)
                        .projectListItemUiModelList[projectListItemUiModel.id].isChecked

                    // update the list of the list item checked state by creating a new list object

                    val newItemList = (_homeUiState.value as HomeUiState.Success)
                        .projectListItemUiModelList.map { model ->
                            if (model.id == projectListItemUiModel.id) {
                                model.copy(isChecked = !isChecked)
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
    val isChecked: Boolean
)

sealed class HomeUiState {
    data class Success (
        val projectListItemUiModelList: List<ProjectListItemUiModel>
    ): HomeUiState()
    object Loading: HomeUiState()
    object Error: HomeUiState()
}