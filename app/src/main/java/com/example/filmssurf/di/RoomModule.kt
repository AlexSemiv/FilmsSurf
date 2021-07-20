package com.example.filmssurf.di

import android.content.Context
import androidx.room.Room
import com.example.filmssurf.db.FilmsDao
import com.example.filmssurf.db.FilmsDatabase
import com.example.filmssurf.other.Utils.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RoomModule {

    @Provides
    @Singleton
    fun provideFilmsDatabase(@ApplicationContext context: Context): FilmsDatabase = Room.databaseBuilder(
        context.applicationContext,
        FilmsDatabase::class.java,
        DATABASE_NAME
    ).build()

    @Provides
    @Singleton
    fun provideFilmsDao(filmsDatabase: FilmsDatabase): FilmsDao = filmsDatabase.getFilmsDao()
}