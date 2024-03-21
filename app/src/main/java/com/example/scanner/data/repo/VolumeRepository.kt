package com.example.scanner.data.repo

import com.example.scanner.R
import com.example.scanner.data.SQLITE_BATCHSIZE
import com.example.scanner.data.dao.VolumeDao
import com.example.scanner.data.entity.VolumeEntity
import com.example.scanner.ui.viewmodel.VolumeAddUiModel
import com.example.scanner.ui.viewmodel.VolumeEditUiModel
import com.example.scanner.ui.viewmodel.VolumeItemUiModel
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
            volumeSource = "",
            collectionId = collectionId,
            createTime = Date(),
            modifyTime = Date()
        )
        volumeDao.insertOne(volumeEntity)
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

    suspend fun getVolumeSourceMapFromUiModel(volumeItemUiModel: VolumeItemUiModel): Map<String, String> {
        return emptyMap()
    }
}