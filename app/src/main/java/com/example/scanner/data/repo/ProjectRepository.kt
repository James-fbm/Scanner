package com.example.scanner.data.repo

import com.example.scanner.data.entity.ProjectListItemEntity

class ProjectRepository {
    fun getAllProject(): List<ProjectListItemEntity> {
        val projectListItemEntityList = listOf(
            ProjectListItemEntity("项目1", "2022-01-01", "2022-01-02"),
            ProjectListItemEntity("项目2", "2022-02-01", "2022-02-02"),
            ProjectListItemEntity("项目3", "2022-03-01", "2022-03-02"),
            ProjectListItemEntity("项目4", "2022-04-01", "2022-04-02"),
            ProjectListItemEntity("项目5", "2022-05-01", "2022-05-02")
        )

        return projectListItemEntityList
    }
}