package com.example.filmssurf.repository

import com.example.filmssurf.api.FilmsApi
import com.example.filmssurf.db.Film
import com.example.filmssurf.db.FilmsDao
import javax.inject.Inject

class FilmsRepository @Inject constructor(
    private val api: FilmsApi,
    private val dao: FilmsDao
) {

    suspend fun getTopFilms() = api.getTopFilms()

    suspend fun insertFilm(film: Film) = dao.insertFilm(film)

    suspend fun deleteFilm(film: Film) = dao.deleteFilm(film)

    suspend fun getFavoriteFilms() = dao.getFavoriteFilms()

    suspend fun getIdOfFavoriteFilms() = dao.getIfOfFavoriteFilms()
}