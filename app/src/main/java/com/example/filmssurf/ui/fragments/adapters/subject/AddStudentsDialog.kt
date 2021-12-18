package com.example.filmssurf.ui.fragments.adapters.subject

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.courseworkdb.data.StudentSubjectCrossRef
import com.example.filmssurf.R
import com.example.filmssurf.viewmodel.ChangeViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import coursework.courseworkdb.StudentEntity
import coursework.courseworkdb.StudentSubjectCrossRefEntity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class AddStudentsDialog: DialogFragment() {

    private val args: AddStudentsDialogArgs by navArgs()
    private val viewModel: ChangeViewModel by viewModels()
    private var dialogView: View? = null

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
                R.layout.support_simple_spinner_dropdown_item,
                null
            )

        val allStudents: List<StudentEntity>
        val subjectStudents: List<String>
        runBlocking {
            allStudents = viewModel.getAllStudents().first()
            subjectStudents = viewModel.getStudentsBySubjectName(args.subjectName).first().map { it._student_name }
        }
        val checkedItems = allStudents.map { subjectStudents.contains(it._name) }

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(args.subjectName)
            .setMultiChoiceItems(
                allStudents.map { it._name }.toTypedArray(),
                BooleanArray(checkedItems.size) { checkedItems[it] }
            ) { _, i, isChecked ->
                if(isChecked)
                    viewModel.insertStudentSubject(
                        studentName = allStudents[i]._name,
                        subjectName = args.subjectName
                    )
                else
                    viewModel.deleteStudentSubject(
                        studentName = allStudents[i]._name,
                        subjectName = args.subjectName
                    )
            }
            .setPositiveButton("Close") { _, _ -> }
            .setIcon(R.drawable.ic_add_student)
            .create()
    }
}