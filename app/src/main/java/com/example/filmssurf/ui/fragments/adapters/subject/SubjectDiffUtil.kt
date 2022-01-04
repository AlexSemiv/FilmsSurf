package com.example.filmssurf.ui.fragments.adapters.subject

import androidx.recyclerview.widget.DiffUtil
import coursework.courseworkdb.SchoolEntity
import coursework.courseworkdb.StudentEntity
import coursework.courseworkdb.SubjectEntity
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class SubjectDiffUtil @Inject constructor() : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

}