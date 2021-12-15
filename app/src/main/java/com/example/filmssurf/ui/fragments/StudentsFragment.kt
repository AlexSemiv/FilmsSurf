package com.example.filmssurf.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

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
            viewModel?.getAllStudents()?.collect {
                studentAdapter.submitList(it)
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