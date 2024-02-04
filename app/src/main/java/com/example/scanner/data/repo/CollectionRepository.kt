package com.example.scanner.data.repo

import com.example.scanner.data.dao.CollectionDao
import com.example.scanner.data.entity.CollectionEntity
import com.example.scanner.ui.viewmodel.CollectionAddUiModel
import com.example.scanner.ui.viewmodel.CollectionEditUiModel
import com.example.scanner.ui.viewmodel.CollectionItemUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

class CollectionRepository @Inject constructor(
    private val collectionDao: CollectionDao
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

        collectionDao.deleteByIdList(toDeleteIdList)
    }

    suspend fun updateCollectionFromUiModel(collectionEditUiModel: CollectionEditUiModel) {
        val collectionId = collectionEditUiModel.collectionId
        val collectionName = collectionEditUiModel.collectionName
        val modifyTime = Date()

        collectionDao.updateOne(collectionId, collectionName, modifyTime)
    }
}