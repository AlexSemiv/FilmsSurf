package com.example.filmssurf.ui.fragments.adapters.school

import com.example.filmssurf.base.BaseViewHolder
import com.example.filmssurf.databinding.ListItemBinding
import coursework.courseworkdb.SchoolEntity

class SchoolViewHolder(
    val binding: ListItemBinding
) : BaseViewHolder<SchoolEntity, ListItemBinding> (binding) {

    override fun bind() {
        item?.let { school ->
            val text = "\n${school._name}\n" +
                    "specialization: ${school._specialization}\n" +
                    "address: ${school._address}"
            binding.tvItem.text = text
        }
    }
}