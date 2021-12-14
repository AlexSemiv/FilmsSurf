package com.example.filmssurf.ui.fragments.adapters.subject

import com.example.filmssurf.base.BaseViewHolder
import com.example.filmssurf.databinding.ListItemBinding
import coursework.courseworkdb.SchoolEntity
import coursework.courseworkdb.StudentEntity
import coursework.courseworkdb.SubjectEntity

class SubjectViewHolder(
    val binding: ListItemBinding
) : BaseViewHolder<String, ListItemBinding> (binding) {

    override fun bind() {
        item?.let { subject ->
            binding.tvItem.text = subject
        }
    }
}