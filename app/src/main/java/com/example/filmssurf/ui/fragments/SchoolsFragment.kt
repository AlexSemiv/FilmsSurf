package com.example.filmssurf.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filmssurf.R
import com.example.filmssurf.base.BaseFragment
import com.example.filmssurf.databinding.ListLayoutBinding
import com.example.filmssurf.other.SchoolSortType
import com.example.filmssurf.ui.MainActivity
import com.example.filmssurf.ui.fragments.adapters.school.NewSchoolDialog
import com.example.filmssurf.ui.fragments.adapters.school.SchoolAdapter
import com.example.filmssurf.ui.fragments.adapters.student.StudentAdapter
import com.example.filmssurf.viewmodel.TablesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SchoolsFragment : BaseFragment<ListLayoutBinding>() {

    @Inject
    lateinit var schoolAdapter: SchoolAdapter

    @Inject
    lateinit var studentsAdapter: StudentAdapter

    private var viewModel: TablesViewModel? = null

    private var searchJob: Job? = null
    private var sortJob: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

        binding.spSort.apply {
            val fields = listOf("Name", "Address", "Specialization")
            adapter = object : ArrayAdapter<String>(
                requireContext(),
                R.layout.support_simple_spinner_dropdown_item,
                fields
            ) {  }
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    sortJob?.cancel()
                    sortJob = lifecycleScope.launch {
                        viewModel?.setSchoolSortType(when(position) {
                            0 -> SchoolSortType.NAME
                            1 -> SchoolSortType.ADDRESS
                            2 -> SchoolSortType.SPECIALIZATION
                            else -> SchoolSortType.NAME
                        })
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

            }
        }

        binding.svFilms.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean = false

                override fun onQueryTextChange(newText: String?): Boolean {
                    searchJob?.cancel()
                    searchJob = lifecycleScope.launch {
                        newText?.let { query ->
                            viewModel?.setSchoolSearchQuery(query)
                        }
                    }
                    return false
                }
            })
        }

        binding.ibNewItem.setOnClickListener {
            NewSchoolDialog().show(childFragmentManager, null)
        }

        binding.rvFilms.apply {
            adapter = schoolAdapter.apply {
                setShowMoreIcon(R.drawable.ic_student)
                setDeleteListener {
                    viewModel?.deleteSchool(it)
                }
                setChangeListener {
                    findNavController().navigate(
                        R.id.globalActionToSchoolChangeDialog,
                        Bundle().apply {
                            putString("schoolName", it._name)
                        }
                    )
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
                    ShowMoreDialog(
                        "${it._name} students",
                        R.drawable.ic_student,
                        studentsRecyclerView
                    ).show(childFragmentManager, null)

                    viewLifecycleOwner.lifecycleScope.launch {
                        viewModel?.getStudentsBySchoolName(it._name)?.collect { students ->
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
            viewModel?.schools?.collect {
                schoolAdapter.submitList(it)
            }
        }

        viewModel?.schoolSearchQuery?.observe(viewLifecycleOwner) {
            binding.svFilms.setQuery(it, false)
        }

        viewModel?.schoolSortType?.observe(viewLifecycleOwner) {
            binding.spSort.setSelection(
                when(it) {
                    SchoolSortType.NAME -> 0
                    SchoolSortType.ADDRESS -> 1
                    SchoolSortType.SPECIALIZATION -> 2
                }
            )
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