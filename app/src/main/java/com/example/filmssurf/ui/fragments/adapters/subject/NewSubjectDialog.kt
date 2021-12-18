package com.example.filmssurf.ui.fragments.adapters.subject

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.filmssurf.R
import com.example.filmssurf.viewmodel.ChangeViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewSubjectDialog : DialogFragment() {

    private val viewModel: ChangeViewModel by viewModels()
    private var dialogView: View? = null

    private var etName: EditText? = null

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
                R.layout.subject_change_dialog,
                null
            )

        dialogView?.let {
            etName = it.findViewById(R.id.etSubjectName)
        }

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle("New subject")
            .setView(dialogView!!)
            .setPositiveButton("Create") { _, _ ->
                if(etName?.text.toString().isEmpty()) {
                    Toast.makeText(requireContext(), "Fields can't be empty", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.createSubject(
                        name = etName?.text.toString()
                    )
                }
            }
            .setNegativeButton("Cancel") { _, _ -> }
            .setIcon(R.drawable.ic_subject)
            .create()
    }
}