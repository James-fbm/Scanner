package com.example.scanner.data.repo

import com.example.scanner.data.entity.ProjectListItemEntity

class ProjectRepository {
    fun getAllProject(): List<ProjectListItemEntity> {
        val projectListItemEntityList = mutableListOf<ProjectListItemEntity>()
        for (i in 1..10) {
            val projectName = "项目$i"
            val startDate = "2022-${String.format("%02d", i % 12 + 1)}-01" // 格式化月份，确保始终是两位数
            val endDate = "2022-${String.format("%02d", i % 12 + 1)}-02" // 使用i的模运算来循环生成月份
            projectListItemEntityList.add(ProjectListItemEntity(projectName, startDate, endDate))
        }

        return projectListItemEntityList
    }
}