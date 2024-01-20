package com.example.scanner.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.scanner.data.entity.SetEntity

@Dao
interface SetDao {
    @Query("SELECT * FROM tb_set WHERE project_id = :projectId")
    suspend fun getSetById(projectId: Int): List<SetEntity>
}