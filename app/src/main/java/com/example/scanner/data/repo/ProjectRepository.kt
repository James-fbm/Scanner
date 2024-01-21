package com.example.scanner.data.repo

import com.example.scanner.data.dao.ProjectDao
import com.example.scanner.data.entity.ProjectEntity
import com.example.scanner.ui.viewmodel.ProjectAddUiModel
import com.example.scanner.ui.viewmodel.ProjectEditUiModel
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
            // id will be ignored here
            projectId = 0,
            projectName = projectAddUiModel.projectName,
            createTime = Date(),
            modifyTime = Date()
        )
        projectDao.insertOne(projectEntity)
    }

    suspend fun deleteProjectFromUiModelList(toDeleteItemUiModelList: List<ProjectItemUiModel>) {

        // Only id(primary key) needs to be set. Other attributes are assigned with a default value.

        val toDeleteIdList = toDeleteItemUiModelList
            .filter { projectItemUiModel -> projectItemUiModel.itemChecked }
            .map { projectItemUiModel -> projectItemUiModel.projectId }

        projectDao.deleteByIdList(toDeleteIdList)
    }

    suspend fun updateProjectFromUiModel(projectEditUiModel: ProjectEditUiModel) {

        val projectId = projectEditUiModel.projectId
        val projectName = projectEditUiModel.projectName
        val modifyTime = Date()
        projectDao.updateOne(projectId, projectName, modifyTime)
    }
}


fun formatDate(date: Date): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return formatter.format(date)
}

fun makeDate(dateString: String): Date {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return sdf.parse(dateString) ?: throw IllegalArgumentException("Invalid date format")
}