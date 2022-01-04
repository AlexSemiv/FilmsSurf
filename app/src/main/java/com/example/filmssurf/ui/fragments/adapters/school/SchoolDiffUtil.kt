package com.example.filmssurf.ui.fragments.adapters.school

import androidx.recyclerview.widget.DiffUtil
import coursework.courseworkdb.SchoolEntity
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class SchoolDiffUtil @Inject constructor() : DiffUtil.ItemCallback<SchoolEntity>() {
    override fun areItemsTheSame(oldItem: SchoolEntity, newItem: SchoolEntity): Boolean {
        return oldItem._name == newItem._name
    }

    override fun areContentsTheSame(oldItem: SchoolEntity, newItem: SchoolEntity): Boolean {
        return oldItem == newItem
    }
}