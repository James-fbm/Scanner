package com.example.scanner.di

import android.content.Context
import androidx.room.Room
import com.example.scanner.data.AppDatabase
import com.example.scanner.data.dao.ProjectDao
import com.example.scanner.data.dao.CollectionDao
import com.example.scanner.data.dao.VolumeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "scanner_cache"
        ).build()
    }

    @Provides
    fun provideProjectDao(appDatabase: AppDatabase): ProjectDao {
        return appDatabase.projectDao()
    }

    @Provides
    fun provideCollectionDao(appDatabase: AppDatabase): CollectionDao {
        return appDatabase.collectionDao()
    }

    @Provides
    fun provideVolumeDao(appDatabase: AppDatabase): VolumeDao {
        return appDatabase.volumeDao()
    }
}