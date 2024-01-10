package com.example.scanner.data.repo

import com.example.scanner.data.dao.ProjectDao
import com.example.scanner.data.entity.ProjectEntity
import javax.inject.Inject

class ProjectRepository @Inject constructor(
    private val projectDao: ProjectDao
) {
    fun getExampleProjectList(): List<ProjectEntity> {
        val projectEntityList = mutableListOf<ProjectEntity>()
        for (i in 1..10) {
            val projectName = "项目$i"
            val startDate = "2022-${String.format("%02d", i % 12 + 1)}-01" // 格式化月份，确保始终是两位数
            val endDate = "2022-${String.format("%02d", i % 12 + 1)}-02" // 使用i的模运算来循环生成月份
            projectEntityList.add(ProjectEntity(i, projectName, startDate, endDate))
        }

        return projectEntityList
    }

    suspend fun getAllProject(): List<ProjectEntity> {
        return projectDao.getAll()
    }
}