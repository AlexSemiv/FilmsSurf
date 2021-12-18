package com.example.filmssurf.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filmssurf.R
import com.example.filmssurf.base.BaseFragment
import com.example.filmssurf.databinding.ListLayoutBinding
import com.example.filmssurf.ui.MainActivity
import com.example.filmssurf.ui.fragments.adapters.student.StudentAdapter
import com.example.filmssurf.ui.fragments.adapters.subject.SubjectAdapter
import com.example.filmssurf.viewmodel.TablesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SubjectsFragment: BaseFragment<ListLayoutBinding>() {

    @Inject
    lateinit var subjectAdapter: SubjectAdapter

    @Inject
    lateinit var studentsAdapter: StudentAdapter

    private var viewModel: TablesViewModel? = null

    private var searchJob: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

        binding.tvSortType.visibility = View.GONE
        binding.spSort.visibility = View.GONE

        binding.svFilms.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean = false

                override fun onQueryTextChange(newText: String?): Boolean {
                    searchJob?.cancel()
                    searchJob = lifecycleScope.launch {
                        newText?.let { query ->
                            viewModel?.setSubjectSearchQuery(query)
                        }
                    }
                    return false
                }
            })
        }

        binding.rvFilms.apply {
            adapter = subjectAdapter.apply {
                setShowMoreIcon(R.drawable.ic_student)
                setAddStudentsListener {
                    findNavController().navigate(
                        R.id.globalActionToAddStudentsDialog,
                        Bundle().apply {
                            putString("subjectName", it._name)
                        }
                    )
                }
                setDeleteListener {
                    viewModel?.deleteSubject(it)
                }
                setShowMoreListener {
                    val studentsRecyclerView = RecyclerView(requireContext()).apply {
                        adapter = studentsAdapter.apply {
                            setShouldShowActions(false)
                        }
                        layoutManager = LinearLayoutManager(requireContext())
                        addItemDecoration(
                            DividerItemDecoration(
                                requireContext(),
                                DividerItemDecoration.VERTICAL
                            )
                        )
                    }
                    ShowMoreDialog(it._name, R.drawable.ic_student, studentsRecyclerView).show(childFragmentManager, null)

                    viewLifecycleOwner.lifecycleScope.launch {
                        viewModel?.getStudentsBySubjectName(it._name)?.collect { students ->
                            studentsAdapter.submitList(students)
                        }
                    }
                }
            }
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel?.subjects?.collect {
                subjectAdapter.submitList(it)
            }
        }

        viewModel?.subjectSearchQuery?.observe(viewLifecycleOwner) {
            binding.svFilms.setQuery(it, false)
        }
    }

    override fun bindLayout(
        inflater: LayoutInflater,
        viewGroup: ViewGroup?,
        attachToRoot: Boolean
    ): ListLayoutBinding {
        return ListLayoutBinding.inflate(inflater, viewGroup, attachToRoot)
    }
}