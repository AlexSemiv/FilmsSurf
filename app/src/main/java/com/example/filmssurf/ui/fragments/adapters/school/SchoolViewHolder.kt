package com.example.filmssurf.ui.fragments.adapters.school

import com.example.filmssurf.base.BaseViewHolder
import com.example.filmssurf.databinding.ListItemBinding
import coursework.courseworkdb.SchoolEntity

class SchoolViewHolder(
    val binding: ListItemBinding
) : BaseViewHolder<SchoolEntity, ListItemBinding> (binding) {

    override fun bind() {
        item?.let { school ->
            binding.tvItem.text = school.toString()
        }
    }
}