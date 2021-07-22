package com.example.filmssurf.other

// generic class that help us to handle all states of data
sealed class Resource<T>(
    var data: T? = null,
    val error: String? = null
) {
    class Success<T>(data: T): Resource<T>(data)
    class Error<T>(error: String, data: T? = null) : Resource<T>(data,error)
    class Loading<T>(data: T? = null) : Resource<T>(data)
}