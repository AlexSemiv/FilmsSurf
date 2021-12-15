package com.example.filmssurf.ui.fragments.adapters.student

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.filmssurf.R
import com.example.filmssurf.viewmodel.ChangeViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StudentChangeDialog : DialogFragment() {

    private val args: StudentChangeDialogArgs by navArgs()
    private val viewModel: ChangeViewModel by viewModels()
    private var dialogView: View? = null

    private var spSemesters: Spinner? = null
    private var spSchools: Spinner? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return dialogView
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialogView = LayoutInflater.from(requireContext())
            .inflate(
                R.layout.student_change_dialog,
                null
            )

        dialogView?.let {
            spSemesters = it.findViewById(R.id.spSemesters)
            spSchools = it.findViewById(R.id.spSchools)
        }

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(args.studentName)
            .setView(dialogView!!)
            .setPositiveButton("Change") { _, _ ->
                spSchools?.selectedItemPosition?.let {
                    viewModel.schoolNamesLiveData.value?.get(it)?.let { schoolName ->
                        viewModel.changeExistedStudent(
                            name = args.studentName,
                            semester = spSemesters?.selectedItemPosition?.plus(1L) ?: 1L,
                            schoolName = schoolName
                        )
                    }
                }
            }
            .setNegativeButton("Cancel") { _, _ -> }
            .setIcon(R.drawable.ic_student)
            .create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.schoolNamesLiveData.observe(viewLifecycleOwner) {
            spSchools?.adapter = object : ArrayAdapter<String>(
                requireContext(),
                R.layout.support_simple_spinner_dropdown_item,
                it
            ) {  }
        }

        viewModel.studentLiveData.observe(viewLifecycleOwner) {
            it?.let { student ->
                spSemesters?.setSelection(student._semester.minus(1L).toInt())
                val index = viewModel.schoolNamesLiveData.value?.indexOf(it._school_name)
                spSchools?.setSelection(index ?: 0)
            }
        }

        viewModel.getStudentByName(args.studentName)
    }
}