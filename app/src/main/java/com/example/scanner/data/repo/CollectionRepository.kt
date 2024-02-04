package com.example.scanner.data.repo

import com.example.scanner.data.dao.CollectionDao
import javax.inject.Inject

class CollectionRepository @Inject constructor(
    private val collectionDao: CollectionDao
) {

}