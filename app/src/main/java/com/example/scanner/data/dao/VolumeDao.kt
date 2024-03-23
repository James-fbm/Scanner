package com.example.scanner.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.scanner.data.SQLITE_BATCHSIZE
import com.example.scanner.data.entity.VolumeEntity
import com.example.scanner.data.entity.VolumeSourceEntity
import com.example.scanner.data.repo.VolumeDBRecord
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface VolumeDao {
    @Query("SELECT * FROM tb_volume WHERE collection_id = :collectionId")
    fun getVolumeByCollectionId(collectionId: Int): Flow<List<VolumeEntity>>

    @Query("SELECT * FROM tb_volume WHERE volume_id = :volumeId")
    fun getOneVolumeByVolumeId(volumeId: Int): VolumeEntity

    @Query("SELECT * FROM tb_volume_source WHERE volume_id = :volumeId")
    fun getVolumeSourceByVolumeId(volumeId: Int): List<VolumeSourceEntity>

    @Insert
    suspend fun insertOneEntity(volumeEntity: VolumeEntity): Long

    @Insert
    suspend fun insertEntityList(volumeEntityList: List<VolumeEntity>): List<Long>

    @Insert
    suspend fun insertSourceEntityList(volumeSourceEntityList: List<VolumeSourceEntity>)

    @Transaction
    suspend fun insertVolumeRecord(volumeRecord: VolumeDBRecord) {
        val entityDBList: List<VolumeEntity> = volumeRecord.map { entityPair ->
            entityPair.first
        }
        val entityIdList = insertEntityList(entityDBList)

        val sourceEntityDBList: List<VolumeSourceEntity> = volumeRecord.flatMapIndexed { index, entityPair ->
            entityPair.second.map { sourceEntity ->
                VolumeSourceEntity(
                    sourceId = sourceEntity.sourceId,
                    sourceLine = sourceEntity.sourceLine,
                    volumeId = entityIdList[index].toInt()
                )
            }
        }
        
        insertSourceEntityList(sourceEntityDBList)
    }

    @Query("DELETE FROM tb_volume WHERE volume_id in (:idList)")
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

    @Query("UPDATE tb_volume SET volume_name = :volumeName, modify_time = :modifyTime WHERE volume_id = :volumeId")
    suspend fun updateOne(volumeId: Int, volumeName: String, modifyTime: Date)
}