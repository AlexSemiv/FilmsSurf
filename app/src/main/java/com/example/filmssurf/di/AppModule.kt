package com.example.filmssurf.di

import android.app.Application
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.courseworkdb.CourseDatabase
import com.example.filmssurf.data.DataSource
import com.example.filmssurf.data.DataSourceImpl
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSqlDriver(
        app: Application
    ): SqlDriver {
        return AndroidSqliteDriver(
            schema = CourseDatabase.Schema,
            context = app,
            name = "course.db",
            callback = object : AndroidSqliteDriver.Callback(CourseDatabase.Schema) {
                override fun onOpen(db: SupportSQLiteDatabase) {
                    db.execSQL("PRAGMA foreign_keys=ON;");
                }
            }
        )
    }

    @Provides
    @Singleton
    fun provideDataSource(
        driver: SqlDriver
    ): DataSource {
        return DataSourceImpl(
            source = CourseDatabase.invoke(
                driver = driver
            )
        )
    }
}