package com.example.scanner.data.repo

import com.example.scanner.csvLineToArray
import com.example.scanner.data.dao.VolumeDao
import com.example.scanner.data.entity.VolumeEntity
import com.example.scanner.ui.viewmodel.VolumeAddUiModel
import com.example.scanner.ui.viewmodel.VolumeEditUiModel
import com.example.scanner.ui.viewmodel.VolumeItemUiModel
import com.example.scanner.ui.viewmodel.VolumeViewUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

class VolumeRepository @Inject constructor(
    private val volumeDao: VolumeDao
) {

    suspend fun getAllVolumeByCollectionIdAsUiModel(collectionId: Int): Flow<List<VolumeItemUiModel>> {
        return volumeDao.getVolumeByCollectionId(collectionId).map { entityList ->
            entityList.map { entity ->
                VolumeItemUiModel(
                    volumeId = entity.volumeId,
                    volumeName = entity.volumeName,
                    modifyTime = formatDate(entity.modifyTime),
                    itemChecked = false,
                    menuVisible = false
                )
            }
        }
    }

    suspend fun insertOneVolumeFromUiModel(collectionId: Int, volumeAddUiModel: VolumeAddUiModel) {
        val volumeEntity = VolumeEntity(
            // id will be ignored here
            volumeId = 0,
            volumeName = volumeAddUiModel.volumeName,
            collectionId = collectionId,
            fromExcel = false,
            // manually added volumes do not have titleLine and indexLine attributes
            titleLine = "",
            indexLine = "",
            createTime = Date(),
            modifyTime = Date()
        )
        volumeDao.insertOneEntity(volumeEntity)
    }

    suspend fun deleteVolumeFromUiModelList(toDeleteItemUiModelList: List<VolumeItemUiModel>) {

        val toDeleteIdList = toDeleteItemUiModelList
            .filter { volumeItemUiModel -> volumeItemUiModel.itemChecked }
            .map { volumeItemUiModel -> volumeItemUiModel.volumeId }

        volumeDao.deleteByIdListInBatch(toDeleteIdList)
    }

    suspend fun updateVolumeFromUiModel(volumeEditUiModel: VolumeEditUiModel) {

        val volumeId = volumeEditUiModel.volumeId
        val volumeName = volumeEditUiModel.volumeName
        val modifyTime = Date()
        volumeDao.updateOne(volumeId, volumeName, modifyTime)
    }

    suspend fun getVolumeViewUiModelFromUiModel(volumeItemUiModel: VolumeItemUiModel): VolumeViewUiModel {
        val volumeEntity = volumeDao.getOneVolumeByVolumeId(volumeItemUiModel.volumeId)
        val volumeSourceEntityList = volumeDao.getVolumeSourceByVolumeId(volumeItemUiModel.volumeId)

        val titleArray = csvLineToArray(volumeEntity.titleLine)
        val indexArray = csvLineToArray(volumeEntity.indexLine)
        val sourceTripleList = volumeSourceEntityList.map { sourceEntity ->
            val sourceArray = csvLineToArray(sourceEntity.sourceLine)
            titleArray.mapIndexed { index, _ ->
                if (index.toString() in indexArray)
                    Triple(titleArray[index], sourceArray[index], true)
                else
                    Triple(titleArray[index], sourceArray[index], false)
            }
        }
        return VolumeViewUiModel(
            volumeName = volumeEntity.volumeName,
            fromExcel = volumeEntity.fromExcel,
            volumeSource = sourceTripleList,
            dialogVisible = true
        )
    }
}