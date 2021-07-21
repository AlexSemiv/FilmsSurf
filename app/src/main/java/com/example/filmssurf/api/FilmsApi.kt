package com.example.filmssurf.api

import com.example.filmssurf.other.Utils.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FilmsApi {

    @GET("discover/movie")
    suspend fun getTopFilms(
        @Query("api_key")
        api_key: String = API_KEY,
        @Query("language")
        language: String = "ru-RU",
        @Query("region")
        region: String = "RU",
        @Query("sort_by")
        sort_by: String = "popularity.desc",
        @Query("include_adult")
        include_adult: Boolean = false,
        @Query("include_video")
        include_video: Boolean = false,
        @Query("page")
        page: Int = 1,
        @Query("with_watch_monetization_types")
        with_watch_monetization_types: String = "flatrate"
    ) : Response<Result>

    @GET("search/movie")
    suspend fun searchFilms(
        @Query("api_key")
        api_key: String = API_KEY,
        @Query("language")
        language: String = "ru-RU",
        @Query("query")
        query: String,
        @Query("page")
        page: Int = 1,
        @Query("include_adult")
        include_adult: Boolean = false,
        @Query("region")
        region: String = "RU"
    ) : Response<Result>
}