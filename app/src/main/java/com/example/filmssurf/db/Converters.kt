package com.example.filmssurf.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromListGenreId(list: List<Int>): String {

        val type = object : TypeToken<List<Int>>() {

        }.type

        return Gson().toJson(list, type)
    }

    @TypeConverter
    fun toListGenreId(string: String): List<Int> {

        val type = object : TypeToken<List<Int>>() {

        }.type

        return Gson().fromJson(string, type)
    }
}