package com.example.filmssurf.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.filmssurf.R
import com.example.filmssurf.databinding.FragmentFilmsBinding
import com.example.filmssurf.db.Film
import com.example.filmssurf.other.Resource
import com.example.filmssurf.other.Utils.DEBUG_TAG
import com.example.filmssurf.other.Utils.TIMEOUT_SEARCHING
import com.example.filmssurf.ui.MainActivity
import com.example.filmssurf.ui.fragments.adapters.FilmsAdapter
import com.example.filmssurf.viewmodel.FilmsViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
abstract class FilmsFragment: Fragment(R.layout.fragment_films) {

    lateinit var viewModel: FilmsViewModel
    private lateinit var filmsAdapter: FilmsAdapter

    abstract val liveData: LiveData<Resource<List<Film>>>
    abstract val refreshFilms: suspend () -> Unit
    abstract val emptyListErrorMessage: String

    private var _binding: FragmentFilmsBinding? = null
    val binding
        get() = _binding
    private var refreshJob: Job? = null
    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFilmsBinding.inflate(inflater, container, false)

        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
        filmsAdapter = FilmsAdapter()

        _binding?.rvFilms?.apply {
            adapter = filmsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        filmsAdapter.apply {
            viewModel.idOfFavoriteFilmsLiveData.observe(viewLifecycleOwner) { filmsId ->
                setOnFavoriteFilmsListener { film ->
                    filmsId.contains(film.id)
                }
            }

            setOnItemClickListener { film ->
                Toast.makeText(requireContext(), film.title, Toast.LENGTH_LONG).show()
            }

            setOnIsCheckedStateChangeListener { film ->
                viewModel.saveFilmToFavorite(film)
            }

            setOnIsNotCheckedStateChangeListener { film ->
                viewModel.deleteFilmFromFavorite(film)
            }
        }

        _binding?.svFilms?.apply {
            isSubmitButtonEnabled = false

            var shouldSearch = false

            setOnQueryTextFocusChangeListener { _, hasFocus ->
                shouldSearch = hasFocus
            }

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?) = false

                override fun onQueryTextChange(newText: String?): Boolean {
                    if(shouldSearch) {
                        searchJob?.cancel()
                        searchJob = lifecycleScope.launch {
                            delay(TIMEOUT_SEARCHING)
                            if (isActive) {
                                newText?.let { query ->
                                    if (query.length > 1) {
                                        Log.d(DEBUG_TAG, "searching $query ..")
                                        viewModel.showFilmsInStartFragment(query)
                                    }
                                }
                            }
                        }
                    }
                    return true
                }
            })
        }

        _binding?.srlFilms?.apply {
            setOnRefreshListener {
                refreshJob?.cancel()
                refreshJob = lifecycleScope.launch {
                    if(isActive) {
                        Log.d(DEBUG_TAG, "refreshing ..")
                        refreshFilms.invoke()
                    }
                }
                isRefreshing = false
            }
        }

        liveData.observe(viewLifecycleOwner) { result ->
            hideErrorLayout()
            when(result) {
                is Resource.Success -> {
                    hideCircleProgressBar()
                    hideHorizontalProgressBar()
                    result.data?.let { films ->
                        if(films.isNotEmpty())
                            filmsAdapter.differ.submitList(films)
                        else {
                            filmsAdapter.differ.submitList(listOf())
                            showErrorLayout(emptyListErrorMessage, R.drawable.ic_big_search)
                        }
                    }
                }
                is Resource.Error -> {
                    hideCircleProgressBar()
                    hideHorizontalProgressBar()
                    result.data?.let { films ->
                        filmsAdapter.differ.submitList(films)
                    }
                    result.error?.let { message ->
                        if(filmsAdapter.differ.currentList.isEmpty())
                            showErrorLayout(message, R.drawable.ic_alert_triangle)
                        else
                            Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    result.data?.let { films ->
                        if(films.isNotEmpty())
                            showHorizontalProgressBar()
                        else
                            showCircleProgressBar()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun hideCircleProgressBar() {
        _binding?.pbCircleLoading?.visibility = View.GONE
    }

    private fun showCircleProgressBar() {
        _binding?.pbCircleLoading?.visibility = View.VISIBLE
    }

    private fun hideErrorLayout() {
        _binding?.llError?.visibility = View.GONE
    }

    private fun showErrorLayout(message: String, icon: Int){
        _binding?.llError?.visibility = View.VISIBLE
        _binding?.tvErrorMessage?.text = message
        _binding?.ivErrorIcon?.setImageResource(icon)
    }

    private fun showHorizontalProgressBar() {
        _binding?.pbHorizontalLoading?.visibility = View.VISIBLE
    }

    private fun hideHorizontalProgressBar() {
        _binding?.pbHorizontalLoading?.visibility = View.INVISIBLE
    }
}