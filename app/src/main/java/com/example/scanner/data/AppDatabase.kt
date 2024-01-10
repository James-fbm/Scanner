package com.example.scanner.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.scanner.data.dao.ProjectDao
import com.example.scanner.data.entity.ProjectEntity

@Database(
    entities = [ProjectEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun projectDao(): ProjectDao
}