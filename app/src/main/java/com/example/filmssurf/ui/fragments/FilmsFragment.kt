package com.example.filmssurf.ui.fragments

import android.os.Bundle
import android.util.Log
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
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

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
        filmsAdapter = FilmsAdapter(viewModel.idOfFavoriteFilms)

        binding?.rvFilms?.apply {
            adapter = filmsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        filmsAdapter.apply {
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

        liveData.observe(viewLifecycleOwner) { result -> when(result) {
                is Resource.Success -> {
                    result.data?.let { films ->
                        //Log.d("Debug", films[0].toString())
                        hideSimpleProgressBar()
                        filmsAdapter.differ.submitList(films)
                    }
                }
                is Resource.Error -> {
                    TODO("error")
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
}