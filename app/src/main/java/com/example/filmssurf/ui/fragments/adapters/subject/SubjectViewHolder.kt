package com.example.filmssurf.ui.fragments.adapters.subject

import com.example.filmssurf.base.BaseViewHolder
import com.example.filmssurf.databinding.ListItemBinding

class SubjectViewHolder(
    val binding: ListItemBinding
) : BaseViewHolder<String, ListItemBinding> (binding) {

    override fun bind() {
        item?.let { subject ->
            val text = "\n$subject"
            binding.tvItem.text = text
        }
    }
}