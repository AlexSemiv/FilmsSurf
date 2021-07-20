package com.example.filmssurf.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.filmssurf.db.Film
import com.example.filmssurf.other.Resource
import com.example.filmssurf.repository.FilmsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilmsViewModel @Inject constructor(
    application: Application,
    private val repository: FilmsRepository
): AndroidViewModel(application) {

    private val _startLiveData: MutableLiveData<Resource<List<Film>>> = MutableLiveData()
    val startLiveData: LiveData<Resource<List<Film>>> = _startLiveData
    private lateinit var startFilms: List<Film>

    private val _favoriteLiveData: MutableLiveData<Resource<List<Film>>> = MutableLiveData()
    val favoriteLiveData: LiveData<Resource<List<Film>>> = _favoriteLiveData
    private lateinit var favoriteFilms: List<Film>

    var idOfFavoriteFilms = listOf<Int>()

    init {
        getIdOfFavoriteFilms()
        setStartFilms()
        setFavoriteFilms()
    }

    private fun setStartFilms() = viewModelScope.launch {
        _startLiveData.postValue(Resource.Loading())
        startFilms = repository.getTopFilms().body()!!.results
        _startLiveData.postValue(Resource.Success(startFilms))
    }

    private fun setFavoriteFilms() = viewModelScope.launch {
        _favoriteLiveData.postValue(Resource.Loading())
        favoriteFilms = repository.getFavoriteFilms()
        _favoriteLiveData.postValue(Resource.Success(favoriteFilms))
    }

    private fun getIdOfFavoriteFilms() = viewModelScope.launch(Dispatchers.IO) {
        idOfFavoriteFilms = repository.getIdOfFavoriteFilms()
    }

    fun saveFilmToFavorite(film: Film) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertFilm(film)
        getIdOfFavoriteFilms()
        setFavoriteFilms()
    }

    fun deleteFilmFromFavorite(film: Film) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteFilm(film)
        getIdOfFavoriteFilms()
        setFavoriteFilms()
    }
}