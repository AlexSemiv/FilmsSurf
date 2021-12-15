package com.example.filmssurf.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

        binding.rvFilms.apply {
            adapter = subjectAdapter.apply {
                setShowMoreIcon(R.drawable.ic_student)
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
            viewModel?.getAllSubjects()?.collect {
                Log.d("DEBUG_TAG", "subjects: $it")
                subjectAdapter.submitList(it)
            }
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