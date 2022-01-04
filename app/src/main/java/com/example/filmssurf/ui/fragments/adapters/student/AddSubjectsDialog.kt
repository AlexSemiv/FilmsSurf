package com.example.filmssurf.ui.fragments.adapters.student

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.filmssurf.R
import com.example.filmssurf.viewmodel.ChangeViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first

@AndroidEntryPoint
class AddSubjectsDialog : DialogFragment() {

    private val args: AddSubjectsDialogArgs by navArgs()
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

        val allSubjects: Array<String>
        val studentSubjects: List<String>
        runBlocking {
            allSubjects = viewModel.getAllSubjects().first().toTypedArray()
            studentSubjects = viewModel.getSubjectsByStudentName(args.studentName).first()
        }
        val checkedItems = allSubjects.map { studentSubjects.contains(it) }

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(args.studentName)
            .setMultiChoiceItems(
                allSubjects,
                BooleanArray(checkedItems.size) { checkedItems[it] }
            ) { _, i, isChecked ->
                if(isChecked)
                    viewModel.insertStudentSubject(
                        studentName = args.studentName,
                        subjectName = allSubjects[i]
                    )
                else
                    viewModel.deleteStudentSubject(
                        studentName = args.studentName,
                        subjectName = allSubjects[i]
                    )
            }
            .setPositiveButton("Close") { _, _ -> }
            .setIcon(R.drawable.ic_add_subject)
            .create()

    }
}