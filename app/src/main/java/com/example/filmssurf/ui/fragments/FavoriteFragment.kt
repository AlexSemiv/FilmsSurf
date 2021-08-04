package com.example.filmssurf.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.LiveData
import com.example.filmssurf.db.Film
import com.example.filmssurf.other.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job

@AndroidEntryPoint
class FavoriteFragment: FilmsFragment() {
    override val liveData: LiveData<Resource<List<Film>>>
        get() = viewModel.favoriteLiveData

    override val refreshing: () -> Job
        get() = { viewModel.setFavoriteFilms() }

    override val emptyListErrorMessage: String
        get() = "Вы пока что не добавили ни один фильм в \"Избранное\"..."

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.svFilms?.visibility = View.GONE
    }
}