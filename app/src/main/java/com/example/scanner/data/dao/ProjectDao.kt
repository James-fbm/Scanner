package com.example.scanner.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.scanner.data.entity.ProjectEntity
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface ProjectDao {
    @Query("SELECT * FROM tb_project")
    fun getAll(): Flow<List<ProjectEntity>>

    @Insert
    suspend fun insertOne(projectEntity: ProjectEntity)

    @Query("DELETE FROM tb_project WHERE project_id in (:idList)")
    suspend fun deleteByIdList(idList: List<Int>)

    @Query("UPDATE tb_project SET project_name = :projectName, modify_time = :modifyTime WHERE project_id = :projectId")
    suspend fun updateOne(projectId: Int, projectName: String, modifyTime: Date)
}