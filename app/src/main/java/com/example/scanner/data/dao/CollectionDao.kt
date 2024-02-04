package com.example.scanner.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.scanner.data.entity.CollectionEntity

@Dao
interface CollectionDao {
    @Query("SELECT * FROM tb_collection WHERE project_id = :projectId")
    suspend fun getSetById(projectId: Int): List<CollectionEntity>
}