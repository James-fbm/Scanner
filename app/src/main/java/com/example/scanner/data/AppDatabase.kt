package com.example.scanner.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.scanner.data.dao.ProjectDao
import com.example.scanner.data.dao.SetDao
import com.example.scanner.data.entity.ProjectEntity
import com.example.scanner.data.entity.SetEntity

@Database(
    entities = [ProjectEntity::class, SetEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun projectDao(): ProjectDao
    abstract fun setDao(): SetDao
}