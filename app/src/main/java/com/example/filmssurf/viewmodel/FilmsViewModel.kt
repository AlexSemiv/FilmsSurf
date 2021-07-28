package com.example.filmssurf.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.filmssurf.FilmsApplication
import com.example.filmssurf.db.Film
import com.example.filmssurf.other.*
import com.example.filmssurf.repository.FilmsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class FilmsViewModel @Inject constructor(
    application: Application,
    private val repository: FilmsRepository
): AndroidViewModel(application) {

    private val _startLiveData: MutableLiveData<Resource<List<Film>>> = MutableLiveData()
    val startLiveData: LiveData<Resource<List<Film>>> = _startLiveData

    private val _favoriteLiveData: MutableLiveData<Resource<List<Film>>> = MutableLiveData()
    val favoriteLiveData: LiveData<Resource<List<Film>>> = _favoriteLiveData

    var idOfFavoriteFilmsLiveData = repository.getIdOfFavoriteFilms()
    private var _startFilms: List<Film>
    private var _favoriteFilms: List<Film>

    init {
        _startFilms = listOf()
        _favoriteFilms = listOf()

        setFilmsInStartFragment()
        setFavoriteFilms()
    }

    fun setFilmsInStartFragment(query: String? = null) = viewModelScope.launch {
        _startLiveData.postValue(Resource.Loading(_startFilms))
        try {
            if(hasInternetConnection()) {
                _startFilms = if(query != null)
                    repository.searchFilms(query)?.results ?: listOf()
                else
                    repository.getTopFilms()?.results ?: listOf()
                _startLiveData.postValue(Resource.Success(_startFilms))
            } else {
                _startLiveData.postValue(Resource.Error("Проверьте интернет соединение.\nНевозможо выполнить загрузку.", _startFilms))
            }
        } catch (e: IOException) {
            when(e) {
                is ApiLimitException, is InvalidApiKeyException,
                is InvalidFormatException, is ServiceOfflineException,
                is InternalErrorException, is TimeoutRequestException -> {
                    _startLiveData.postValue(Resource.Error(e.message!!, _startFilms))
                }
                else -> {
                    _startLiveData.postValue(Resource.Error(e.message ?: "IOException", _startFilms))
                }
            }
        }
    }

    fun setFavoriteFilms() = viewModelScope.launch {
        _favoriteLiveData.postValue(Resource.Loading(_favoriteFilms))
        _favoriteFilms = repository.getFavoriteFilms()
        _favoriteLiveData.postValue(Resource.Success(_favoriteFilms))
    }

    fun saveFilmToFavorite(film: Film) = viewModelScope.launch(Dispatchers.IO) {
        film.isFavorite = true
        repository.insertFilm(film)
        setFavoriteFilms()
    }

    fun deleteFilmFromFavorite(film: Film) = viewModelScope.launch(Dispatchers.IO) {
        film.isFavorite = false
        repository.deleteFilm(film)
        setFavoriteFilms()
    }


    // check internet
    @Suppress("DEPRECATION")
    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<FilmsApplication>()
            .getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager
                .getNetworkCapabilities(activeNetwork) ?: return false

            return when{
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run{
                return when(type){
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    NetworkCapabilities.TRANSPORT_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}