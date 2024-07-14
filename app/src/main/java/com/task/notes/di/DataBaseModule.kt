package com.task.notes.di

import android.content.Context
import androidx.room.Room
import com.task.notes.data.local.LocalDataBase
import com.task.notes.data.local.NoteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    fun provideChannelDao(appDatabase: LocalDataBase): NoteDao {
        return appDatabase.dao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): LocalDataBase {
        return Room.databaseBuilder(
            appContext,
            LocalDataBase::class.java,
            "Wear_database"
        ).build()
    }
}