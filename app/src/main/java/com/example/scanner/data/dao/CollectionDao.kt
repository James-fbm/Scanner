package com.example.scanner.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.scanner.data.entity.CollectionEntity
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface CollectionDao {
    @Query("SELECT * FROM tb_collection WHERE project_id = :projectId")
    fun getCollectionByProjectId(projectId: Int): Flow<List<CollectionEntity>>

    @Insert
    suspend fun insertOne(collectionEntity: CollectionEntity): Long

    @Query("DELETE FROM tb_collection WHERE collection_id in (:idList)")
    suspend fun deleteByIdList(idList: List<Int>)

    @Query("UPDATE tb_collection SET collection_name = :collectionName, modify_time = :modifyTime WHERE collection_id = :collectionId")
    suspend fun updateOne(collectionId: Int, collectionName: String, modifyTime: Date)
}