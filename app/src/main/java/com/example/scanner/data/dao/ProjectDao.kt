package com.example.scanner.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.scanner.data.SQLITE_BATCHSIZE
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

    @Transaction
    suspend fun deleteByIdListInBatch(idList: List<Int>) {
        val batchSize = SQLITE_BATCHSIZE
        for (i in idList.indices step batchSize) {
            val end = minOf(i + batchSize, idList.size)
            val batch = idList.subList(i, end)
            deleteByIdList(batch)
        }
    }

    @Query("UPDATE tb_project SET project_name = :projectName, modify_time = :modifyTime WHERE project_id = :projectId")
    suspend fun updateOne(projectId: Int, projectName: String, modifyTime: Date)
}