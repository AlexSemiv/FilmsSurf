package com.example.filmssurf.ui.fragments.adapters.student

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.filmssurf.R
import com.example.filmssurf.viewmodel.ChangeViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class NewStudentDialog : DialogFragment() {

    private val viewModel: ChangeViewModel by viewModels()
    private var dialogView: View? = null

    private var etName: EditText? = null
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

        dialogView?.let { it ->
            it.findViewById<TextView>(R.id.tvStudentName).also { tv ->
                tv.visibility = View.VISIBLE
            }
            it.findViewById<EditText>(R.id.etSubjectName).also { et ->
                et.visibility = View.VISIBLE
                etName = et
            }
            spSemesters = it.findViewById(R.id.spSemesters)
            spSchools = it.findViewById(R.id.spSchools)
        }

        val allSchoolNames: List<String>
        runBlocking {
            allSchoolNames = viewModel.getAllSchools().first().map { it._name }
        }

        spSchools?.apply {
            adapter = object : ArrayAdapter<String>(
                requireContext(),
                R.layout.support_simple_spinner_dropdown_item,
                allSchoolNames
            ) {  }
        }

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle("New student")
            .setView(dialogView!!)
            .setPositiveButton("Create") { _, _ ->
                if(etName?.text.toString().isEmpty()) {
                    Toast.makeText(requireContext(), "Fields can't be empty", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.createStudent(
                        name = etName?.text.toString(),
                        semester = spSemesters?.selectedItemPosition?.plus(1L) ?: 1L,
                        schoolName = allSchoolNames[(spSchools?.selectedItemPosition) ?: 0]
                    )
                }
            }
            .setNegativeButton("Cancel") { _, _ -> }
            .setIcon(R.drawable.ic_student)
            .create()
    }
}