package com.example.filmssurf.ui.fragments

import android.os.Build
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
    abstract val refreshing: Job

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

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
        filmsAdapter = FilmsAdapter()

        binding?.rvFilms?.apply {
            adapter = filmsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        filmsAdapter.apply {
            viewModel.idOfFavoriteFilmsLiveData.observe(viewLifecycleOwner) { filmsId ->
                setOnFavoriteFilmListener { film ->
                    filmsId.contains(film.id)
                }
            }

            setOnItemCLickListener { films ->
                Toast.makeText(requireContext(), films.title, Toast.LENGTH_LONG).show()
            }

            setOnIsCheckedStateChangeListener { film ->
                viewModel.saveFilmToFavorite(film)
            }

            setOnIsNotCheckedStateChangeListener { film ->
                viewModel.deleteFilmFromFavorite(film)
            }
        }

        binding?.svFilms?.apply {
            isSubmitButtonEnabled = false

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?) = false

                override fun onQueryTextChange(newText: String?): Boolean {
                    searchJob?.cancel()
                    Log.d(DEBUG_TAG, "searchJob active: ${searchJob?.isActive}")
                    searchJob = lifecycleScope.launch(Dispatchers.Default) {
                        if(isActive) {
                            newText?.let { text ->
                                if(text.length > 1){
                                    viewModel.setFilmsInStartFragment(text)
                                    Log.d(DEBUG_TAG, "searching: ${text}..")
                                }
                            }
                        }
                    }
                    return true
                }
            })
        }

        binding?.srlFilms?.apply {
            setOnRefreshListener {
                refreshJob?.cancel()
                Log.d(DEBUG_TAG, "refreshJob active: ${refreshJob?.isActive}")
                refreshJob = lifecycleScope.launch(Dispatchers.Default) {
                    if(isActive) {
                        refreshing.join()
                        Log.d(DEBUG_TAG, "refreshing...")
                    }
                }
                isRefreshing = false
            }
        }

        liveData.observe(viewLifecycleOwner) { result ->
            hideErrorLayout()
            when(result) {
                is Resource.Success -> {
                    hideSimpleProgressBar()
                    showHorizontalProgressBar()
                    result.data?.let { films ->
                        if(films.isNotEmpty())
                            filmsAdapter.differ.submitList(films)
                        else {
                            filmsAdapter.differ.submitList(listOf())
                            showErrorLayout("Ничего не найдено. Попробуйте изменить свой запрос.", R.drawable.ic_big_search)
                        }
                        hideHorizontalProgressBar()
                    }
                }
                is Resource.Error -> {
                    hideSimpleProgressBar()
                    showHorizontalProgressBar()
                    result.data?.let { films ->
                        filmsAdapter.differ.submitList(films)
                    }
                    result.error?.let { message ->
                        if(filmsAdapter.differ.currentList.isEmpty())
                            showErrorLayout(message, R.drawable.ic_alert_triangle)
                        else
                            Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
                    }
                    hideHorizontalProgressBar()
                }
                is Resource.Loading -> {
                    showSimpleProgressBar()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun hideSimpleProgressBar() {
        binding?.pbFilms?.visibility = View.GONE
    }

    private fun showSimpleProgressBar() {
        binding?.pbFilms?.visibility = View.VISIBLE
    }

    private fun hideErrorLayout() {
        binding?.llError?.visibility = View.GONE
    }

    private fun showErrorLayout(message: String, icon: Int){
        binding?.llError?.visibility = View.VISIBLE
        binding?.tvErrorMessage?.text = message
        binding?.ivErrorIcon?.setImageResource(icon)
    }

    private fun showHorizontalProgressBar() {
        binding?.pbHorizontalLoading?.visibility = View.VISIBLE
    }

    private fun hideHorizontalProgressBar() {
        binding?.pbHorizontalLoading?.visibility = View.INVISIBLE
    }
}