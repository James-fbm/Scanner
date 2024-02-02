package com.example.scanner.data.repo

import com.example.scanner.data.dao.SetDao
import javax.inject.Inject

class SetRepository @Inject constructor(
    private val setDao: SetDao
) {

}