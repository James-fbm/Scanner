package com.example.scanner.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.scanner.data.entity.ProjectEntity

@Dao
interface ProjectDao {
    @Query("SELECT * FROM project")
    suspend fun getAll(): List<ProjectEntity>
}