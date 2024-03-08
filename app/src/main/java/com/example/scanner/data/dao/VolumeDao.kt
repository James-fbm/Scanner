package com.example.scanner.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.scanner.data.entity.VolumeEntity
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface VolumeDao {
    @Query("SELECT * FROM tb_volume WHERE collection_id = :collectionId")
    fun getVolumeByCollectionId(collectionId: Int): Flow<List<VolumeEntity>>

    @Insert
    suspend fun insertOne(volumeEntity: VolumeEntity)

    @Insert
    suspend fun insertFromList(volumeEntityList: List<VolumeEntity>)

    @Query("DELETE FROM tb_volume WHERE volume_id in (:idList)")
    suspend fun deleteByIdList(idList: List<Int>)

    @Query("UPDATE tb_volume SET volume_name = :volumeName, modify_time = :modifyTime WHERE volume_id = :volumeId")
    suspend fun updateOne(volumeId: Int, volumeName: String, modifyTime: Date)
}