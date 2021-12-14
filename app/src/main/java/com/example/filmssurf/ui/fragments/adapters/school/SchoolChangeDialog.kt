package com.example.filmssurf.ui.fragments.adapters.school

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.filmssurf.R
import com.example.filmssurf.other.toEditable
import com.example.filmssurf.viewmodel.ChangeViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SchoolChangeDialog : DialogFragment() {

    private val args: SchoolChangeDialogArgs by navArgs()
    private val viewModel: ChangeViewModel by viewModels()
    private var dialogView: View? = null

    private var etName: EditText? = null
    private var etAddress: EditText? = null
    private var etSpecialization: EditText? = null

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
            etName = it.findViewById(R.id.etName)
            etAddress = it.findViewById(R.id.etAddress)
            etSpecialization = it.findViewById(R.id.etSpecialization)
        }

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle("Change school")
            .setView(dialogView!!)
            .create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.schoolLiveData.observe(viewLifecycleOwner) {
            Log.d("DEBUG_TAG", it.toString())
            etName?.text = (it?._name ?: "").toEditable()
            etAddress?.text = (it?._address ?: "").toEditable()
            etSpecialization?.text = (it?._specialization ?: "").toEditable()
        }

        viewModel.getSchoolByName(args.schoolName)
    }
}