package com.example.filmssurf.ui.fragments.adapters.student

import androidx.recyclerview.widget.DiffUtil
import coursework.courseworkdb.SchoolEntity
import coursework.courseworkdb.StudentEntity
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class StudentDiffUtil @Inject constructor() : DiffUtil.ItemCallback<StudentEntity>() {
    override fun areItemsTheSame(oldItem: StudentEntity, newItem: StudentEntity): Boolean {
        return oldItem._name == newItem._name
    }

    override fun areContentsTheSame(oldItem: StudentEntity, newItem: StudentEntity): Boolean {
        return oldItem == newItem
    }
}