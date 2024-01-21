package com.example.scanner.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.scanner.data.entity.ProjectEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Query("SELECT * FROM tb_project")
    fun getAll(): Flow<List<ProjectEntity>>

    @Insert
    suspend fun insertOne(projectEntity: ProjectEntity)
}