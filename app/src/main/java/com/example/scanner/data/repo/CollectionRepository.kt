package com.example.scanner.data.repo

import com.example.scanner.R
import com.example.scanner.data.SQLITE_BATCHSIZE
import com.example.scanner.data.dao.CollectionDao
import com.example.scanner.data.dao.VolumeDao
import com.example.scanner.data.entity.CollectionEntity
import com.example.scanner.data.entity.VolumeEntity
import com.example.scanner.ui.viewmodel.CollectionAddUiModel
import com.example.scanner.ui.viewmodel.CollectionEditUiModel
import com.example.scanner.ui.viewmodel.CollectionItemUiModel
import com.example.scanner.ui.viewmodel.ExcelImportUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

class CollectionRepository @Inject constructor(
    private val collectionDao: CollectionDao,
    private val volumeDao: VolumeDao
) {
    suspend fun getCollectionByProjectIdAsUiModel(projectId: Int):
            Flow<List<CollectionItemUiModel>> {
        return collectionDao.getCollectionByProjectId(projectId).map {entityList ->
            entityList.map {entity ->
                CollectionItemUiModel(
                    collectionId = entity.collectionId,
                    collectionName = entity.collectionName,
                    modifyTime = formatDate(entity.modifyTime),
                    itemChecked = false,
                    menuVisible = false
                )
            }
        }
    }

    suspend fun insertOneCollectionFromUiModel(projectId: Int, collectionAddUiModel: CollectionAddUiModel) {
        val collectionEntity = CollectionEntity(
            // id will be ignored here
            collectionId = 0,
            collectionName = collectionAddUiModel.collectionName,
            projectId = projectId,
            createTime = Date(),
            modifyTime = Date()
        )

        collectionDao.insertOne(collectionEntity)
    }

    suspend fun deleteCollectionFromUiModelList(toDeleteItemUiModelList: List<CollectionItemUiModel>) {
        val toDeleteIdList = toDeleteItemUiModelList
            .filter { collectionItemUiModel -> collectionItemUiModel.itemChecked }
            .map { collectionItemUiModel -> collectionItemUiModel.collectionId }

        val batchSize = SQLITE_BATCHSIZE

        for (i in toDeleteIdList.indices step batchSize) {
            val end = minOf(i + batchSize, toDeleteIdList.size)
            val batch = toDeleteIdList.subList(i, end)
            collectionDao.deleteByIdList(batch)
        }
    }

    suspend fun updateCollectionFromUiModel(collectionEditUiModel: CollectionEditUiModel) {
        val collectionId = collectionEditUiModel.collectionId
        val collectionName = collectionEditUiModel.collectionName
        val modifyTime = Date()

        collectionDao.updateOne(collectionId, collectionName, modifyTime)
    }

    suspend fun importCollectionFromExcelFile(projectId: Int, excelImportUiModel: ExcelImportUiModel,
                                              indexIdArray: IntArray): Int {
        val record = readExcelRecord(excelImportUiModel.filePath, excelImportUiModel.fileType, indexIdArray) ?: return 0

        val collectionEntity = CollectionEntity(
            // id will be ignored here
            collectionId = 0,
            collectionName = excelImportUiModel.collectionAlias,
            projectId = projectId,
            createTime = Date(),
            modifyTime = Date()
        )

        val newCollectionId = collectionDao.insertOne(collectionEntity)
        val toInsertVolumeList: List<VolumeEntity> = record.map {r ->
            var volumeIndex: String = ""
            for ((index, e) in r.key.withIndex()) {
                volumeIndex += e
                if (index != r.key.size - 1) {
                    volumeIndex += '_'
                }
            }

            VolumeEntity(
                // id will be ignored here
                volumeId = 0,
                volumeName = volumeIndex,
                volumeSource = "",
                collectionId = newCollectionId.toInt(),
                createTime = Date(),
                modifyTime = Date()
            )
        }

        volumeDao.insertFromList(toInsertVolumeList)
        return record.keys.size
    }
}

typealias IndexRecord = Map<Array<String>, Array<Array<String>>>?
external fun readExcelRecord(filePath: String, fileType: String, indexIdArray: IntArray): IndexRecord