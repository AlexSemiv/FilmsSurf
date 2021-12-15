package com.example.filmssurf.ui.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ShowMoreDialog(
    private val title: String,
    private val showMoreRecycler: RecyclerView
): DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setView(showMoreRecycler)
            .setPositiveButton("OK") { _, _ -> }
            .create()
    }
}