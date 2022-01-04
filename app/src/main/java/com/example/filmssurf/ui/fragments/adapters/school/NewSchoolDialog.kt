package com.example.filmssurf.ui.fragments.adapters.school

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
class NewSchoolDialog : DialogFragment() {

    private val viewModel: ChangeViewModel by viewModels()
    private var dialogView: View? = null

    private var etAddress: EditText? = null
    private var etSpecialization: EditText? = null
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
                R.layout.school_change_dialog,
                null
            )

        dialogView?.let {
            it.findViewById<TextView>(R.id.tvSchoolName).also { tv ->
                tv.visibility = View.VISIBLE
            }
            it.findViewById<EditText>(R.id.etSchoolName).also { et ->
                et.visibility = View.VISIBLE
                etName = et
            }
            etAddress = it.findViewById(R.id.etAddress)
            etSpecialization = it.findViewById(R.id.etSpecialization)
        }

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle("New school")
            .setView(dialogView!!)
            .setPositiveButton("Create") { _, _ ->
                if(etAddress?.text.toString().isEmpty()
                    || etSpecialization?.text.toString().isEmpty()
                    || etName?.text.toString().isEmpty()
                ) {
                    Toast.makeText(requireContext(), "Fields can't be empty", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.createSchool(
                        name = etName?.text.toString(),
                        specialization = etSpecialization?.text.toString(),
                        address = etAddress?.text.toString()
                    )
                }
            }
            .setNegativeButton("Cancel") { _, _ -> }
            .setIcon(R.drawable.ic_school)
            .create()
    }
}