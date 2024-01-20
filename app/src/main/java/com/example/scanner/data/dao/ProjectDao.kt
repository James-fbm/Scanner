package com.example.scanner.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.scanner.data.entity.ProjectEntity

@Dao
interface ProjectDao {
    @Query("SELECT * FROM tb_project")
    suspend fun getAll(): List<ProjectEntity>

    @Insert
    suspend fun insertOne(projectEntity: ProjectEntity)
}