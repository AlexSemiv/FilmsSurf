package com.example.filmssurf.ui.fragments

import androidx.lifecycle.LiveData
import com.example.filmssurf.db.Film
import com.example.filmssurf.other.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment: FilmsFragment() {
    override val liveData: LiveData<Resource<List<Film>>>
        get() = viewModel.favoriteLiveData

}