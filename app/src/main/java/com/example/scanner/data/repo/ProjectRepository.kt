package com.example.scanner.data.repo

import androidx.compose.runtime.collectAsState
import com.example.scanner.data.dao.ProjectDao
import com.example.scanner.data.entity.ProjectEntity
import com.example.scanner.ui.viewmodel.HomeUiState
import com.example.scanner.ui.viewmodel.ProjectAddUiModel
import com.example.scanner.ui.viewmodel.ProjectItemUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class ProjectRepository @Inject constructor(
    private val projectDao: ProjectDao
) {
    suspend fun getAllProjectAsUiModel(): Flow<List<ProjectItemUiModel>> {
        return projectDao.getAll().map { entityList ->
            entityList.map { entity ->
                ProjectItemUiModel(
                    projectId = entity.projectId,
                    projectName = entity.projectName,
                    modifyTime = formatDate(entity.modifyTime),
                    itemChecked = false,
                    menuVisible = false
                )
            }
        }
    }

    suspend fun insertOneProjectFromUiModel(projectAddUiModel: ProjectAddUiModel) {
        val projectEntity = ProjectEntity(
            // will be ignored
            projectId = 0,
            projectName = projectAddUiModel.projectName,
            createTime = Date(),
            modifyTime = Date()
        )
        projectDao.insertOne(projectEntity)
    }

    suspend fun deleteProjectFromUiModel(toDeleteItemUiModelList: List<ProjectItemUiModel>) {

        val defaultDate = Date()

        val toDeleteEntityList = toDeleteItemUiModelList
            .filter { projectItemUiModel -> projectItemUiModel.itemChecked }
            .map { projectItemUiModel -> ProjectEntity (
                projectId = projectItemUiModel.projectId,
                projectName = "",
                createTime = defaultDate,
                modifyTime = defaultDate,
            ) }
        projectDao.deleteByEntityList(toDeleteEntityList)
    }
}


fun formatDate(date: Date): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return formatter.format(date)
}