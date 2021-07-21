package com.example.filmssurf.ui.fragments

import android.os.Bundle
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.filmssurf.R
import com.example.filmssurf.databinding.FragmentFilmsBinding
import com.example.filmssurf.db.Film
import com.example.filmssurf.other.Resource
import com.example.filmssurf.ui.MainActivity
import com.example.filmssurf.ui.fragments.adapters.FilmsAdapter
import com.example.filmssurf.viewmodel.FilmsViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class FilmsFragment: Fragment(R.layout.fragment_films) {

    lateinit var viewModel: FilmsViewModel
    abstract val liveData: LiveData<Resource<List<Film>>>

    private var _binding: FragmentFilmsBinding? = null
    private val binding
        get() = _binding

    lateinit var filmsAdapter: FilmsAdapter

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