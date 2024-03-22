package com.example.scanner.data.repo

import com.example.scanner.arrayToCsvLine
import com.example.scanner.csvLineToArray
import com.example.scanner.data.dao.CollectionDao
import com.example.scanner.data.dao.VolumeDao
import com.example.scanner.data.entity.CollectionEntity
import com.example.scanner.data.entity.VolumeEntity
import com.example.scanner.data.entity.VolumeSourceEntity
import com.example.scanner.data.entity.VolumeTitleEntity
import com.example.scanner.readExcelHeader
import com.example.scanner.readExcelRecord
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

        collectionDao.deleteByIdListInBatch(toDeleteIdList)
    }

    suspend fun updateCollectionFromUiModel(collectionEditUiModel: CollectionEditUiModel) {
        val collectionId = collectionEditUiModel.collectionId
        val collectionName = collectionEditUiModel.collectionName
        val modifyTime = Date()

        collectionDao.updateOne(collectionId, collectionName, modifyTime)
    }

    suspend fun importCollectionFromExcelFile(projectId: Int, excelImportUiModel: ExcelImportUiModel,
                                              indexIdArray: IntArray): Int {
        val collectionEntity = CollectionEntity(
            // id will be ignored here
            collectionId = 0,
            collectionName = excelImportUiModel.collectionAlias,
            projectId = projectId,
            createTime = Date(),
            modifyTime = Date()
        )

        val newCollectionId = collectionDao.insertOne(collectionEntity)

        val record = readExcelRecord(excelImportUiModel.filePath, excelImportUiModel.fileType, indexIdArray) ?: return 0
        record.map {r ->
            var volumeName: String = ""
            val keyArray = csvLineToArray(r.key)
            for ((index, e) in keyArray.withIndex()) {
                volumeName += e
                if (index != keyArray.size - 1) {
                    volumeName += '_'
                }
            }

            val newVolumeEntity = VolumeEntity(
                // id will be ignored here
                volumeId = 0,
                volumeName = volumeName,
                collectionId = newCollectionId.toInt(),
                createTime = Date(),
                modifyTime = Date()
            )

            val indexIdStringArray = indexIdArray.map { index -> index.toString() }.toTypedArray()
            val titleArray = readExcelHeader(excelImportUiModel.filePath, excelImportUiModel.fileType) ?: return 0
            val newVolumeTitleEntity = VolumeTitleEntity(
                titleId = 0,
                titleLine = arrayToCsvLine(titleArray),
                indexLine = arrayToCsvLine(indexIdStringArray),
                // volumeId refers to the volumeId of newVolumeEntity and can only be fetched after it has been written into database
                volumeId = 0
            )

            val newVolumeSourceEntityArray: MutableList<VolumeSourceEntity> = mutableListOf()
            for (recordLine in r.value) {
                val newVolumeSourceEntity = VolumeSourceEntity(
                    sourceId = 0,
                    sourceLine = recordLine,
                    // same as volumeId in newVolumeTitleEntity
                    volumeId = 0
                )
                newVolumeSourceEntityArray.add(newVolumeSourceEntity)
            }
        }

        return record.keys.size
    }
}