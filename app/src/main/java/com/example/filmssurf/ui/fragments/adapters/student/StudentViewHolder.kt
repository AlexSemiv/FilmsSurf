package com.example.filmssurf.ui.fragments.adapters.student

import com.example.filmssurf.base.BaseViewHolder
import com.example.filmssurf.databinding.ListItemBinding
import coursework.courseworkdb.SchoolEntity
import coursework.courseworkdb.StudentEntity

class StudentViewHolder(
    val binding: ListItemBinding
) : BaseViewHolder<StudentEntity, ListItemBinding> (binding) {

    override fun bind() {
        item?.let { student ->
            binding.tvItem.text = student.toString()
        }
    }
}