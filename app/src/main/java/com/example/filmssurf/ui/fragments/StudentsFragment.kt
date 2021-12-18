package com.example.filmssurf.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filmssurf.R
import com.example.filmssurf.base.BaseFragment
import com.example.filmssurf.databinding.ListLayoutBinding
import com.example.filmssurf.other.SchoolSortType
import com.example.filmssurf.other.StudentSortType
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
class StudentsFragment: BaseFragment<ListLayoutBinding>() {

    @Inject
    lateinit var studentAdapter: StudentAdapter

    @Inject
    lateinit var subjectAdapter: SubjectAdapter

    private var viewModel: TablesViewModel? = null

    private var searchJob: Job? = null
    private var sortJob: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

        binding.spSort.apply {
            val fields = listOf("Name", "Semester", "School")
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
                        viewModel?.setStudentSortType(when(position) {
                            0 -> StudentSortType.NAME
                            1 -> StudentSortType.SEMESTER
                            2 -> StudentSortType.SCHOOL
                            else -> StudentSortType.NAME
                        })
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

            }
        }

        /*binding.svFilms.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean = false

                override fun onQueryTextChange(newText: String?): Boolean {
                    searchJob?.cancel()
                    searchJob = lifecycleScope.launch {
                        newText?.let { query ->
                            viewModel?.searchSchool(query)
                        }
                    }
                    return true
                }
            })
        }*/

        binding.rvFilms.apply {
            adapter = studentAdapter.apply {
                setShowMoreIcon(R.drawable.ic_subject)
                setAddSubjectsListener {
                    findNavController().navigate(
                        R.id.globalActionToAddSubjectsDialog,
                        Bundle().apply {
                            putString("studentName", it._name)
                        }
                    )
                }
                setDeleteListener {
                    viewModel?.deleteStudent(it)
                }
                setChangeListener {
                    findNavController().navigate(
                        R.id.globalActionToStudentChangeDialog,
                        Bundle().apply {
                            putString("studentName", it._name)
                        }
                    )
                }
                setShowMoreListener {
                    val subjectsRecyclerView = RecyclerView(requireContext()).apply {
                        adapter = subjectAdapter.apply {
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
                    ShowMoreDialog("${it._name} subjects", R.drawable.ic_subject, subjectsRecyclerView).show(childFragmentManager, null)

                    viewLifecycleOwner.lifecycleScope.launch {
                        viewModel?.getSubjectsByStudentName(it._name)?.collect { subjects ->
                            subjectAdapter.submitList(subjects)
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
            viewModel?.students?.collect {
                studentAdapter.submitList(it)
            }
        }

        viewModel?.studentSortType?.observe(viewLifecycleOwner) {
            binding.spSort.setSelection(
                when(it) {
                    StudentSortType.NAME -> 0
                    StudentSortType.SEMESTER -> 1
                    StudentSortType.SCHOOL -> 2
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