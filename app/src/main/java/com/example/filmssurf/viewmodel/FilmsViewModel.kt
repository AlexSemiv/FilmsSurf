package com.example.filmssurf.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilmsViewModel @Inject constructor(
    application: Application,
    //private val repository: FilmsRepository
): AndroidViewModel(application) {

    /*private val _startLiveData: MutableLiveData<Resource<List<Film>>> = MutableLiveData()
    val startLiveData: LiveData<Resource<List<Film>>> = _startLiveData

    private val _favoriteLiveData: MutableLiveData<Resource<List<Film>>> = MutableLiveData()
    val favoriteLiveData: LiveData<Resource<List<Film>>> = _favoriteLiveData

    var idOfFavoriteFilmsLiveData = repository.getIdOfFavoriteFilms()
    private var _startFilms: List<Film> = listOf()
    private var _favoriteFilms: List<Film> = listOf()
     */

    init {
        viewModelScope.launch {
            showFilmsInStartFragment()
        }

        viewModelScope.launch {
            showFavoriteFilms()
        }
    }

    suspend fun showFilmsInStartFragment(query: String? = null) {

    }

    suspend fun showFavoriteFilms() {

    }
}