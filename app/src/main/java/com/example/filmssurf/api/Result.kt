package com.example.filmssurf.api

import com.example.filmssurf.db.Film

data class Result(
    val page: Int,
    val results: List<Film>,
    val total_pages: Int,
    val total_results: Int
)