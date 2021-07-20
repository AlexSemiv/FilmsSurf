package com.example.filmssurf.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FilmsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun  insertFilm(film: Film)

    @Delete
    suspend fun deleteFilm(film: Film)

    @Query("SELECT * FROM films")
    suspend fun getFavoriteFilms(): List<Film>

    @Query("SELECT id FROM films")
    fun getIfOfFavoriteFilms(): LiveData<List<Int>>
}