package com.example.filmssurf.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [Film::class],
    version = 1
)
@TypeConverters(
    Converters::class
)
abstract class FilmsDatabase: RoomDatabase() {

    abstract fun getFilmsDao(): FilmsDao
}