package com.example.filmssurf.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
abstract class FilmsFragment: Fragment(R.layout.fragment_films) {

    lateinit var viewModel: FilmsViewModel
    private lateinit var filmsAdapter: FilmsAdapter

    abstract val liveData: LiveData<Resource<List<Film>>>
    abstract val refreshing: Job

    private var _binding: FragmentFilmsBinding? = null
    private val binding
        get() = _binding
    private var refreshJob: Job? = null

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

        binding?.srlFilms?.apply {
            setOnRefreshListener {
                refreshJob?.cancel()
                refreshJob = lifecycleScope.launch(Dispatchers.Default) {
                    if(isActive) {
                        refreshing.join()
                        Log.d(DEBUG_TAG, "refreshing...")

                        withContext(Dispatchers.Main){
                            Toast.makeText(requireContext(), "Refreshing", Toast.LENGTH_SHORT).show()
                        }
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
                    result.data?.let { films ->
                        filmsAdapter.differ.submitList(films)
                    }
                }
                is Resource.Error -> {
                    hideSimpleProgressBar()
                    result.data?.let { films ->
                        filmsAdapter.differ.submitList(films)
                    }
                    result.error?.let { message ->
                        showErrorLayout(message)
                    }
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

    private fun showErrorLayout(message: String){
        binding?.llError?.visibility = View.VISIBLE
        binding?.tvErrorMessage?.text = message
    }
}