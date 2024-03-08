package com.example.scanner.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.scanner.data.dao.ProjectDao
import com.example.scanner.data.dao.CollectionDao
import com.example.scanner.data.dao.VolumeDao
import com.example.scanner.data.entity.ProjectEntity
import com.example.scanner.data.entity.CollectionEntity
import com.example.scanner.data.entity.VolumeEntity

val SQLITE_BATCHSIZE = 100

@Database(
    entities = [ProjectEntity::class, CollectionEntity::class, VolumeEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun projectDao(): ProjectDao
    abstract fun collectionDao(): CollectionDao
    abstract fun volumeDao(): VolumeDao
}