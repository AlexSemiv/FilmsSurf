package com.example.filmssurf.ui.fragments.adapters.school

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.filmssurf.base.BaseRecyclerAdapter
import com.example.filmssurf.databinding.ListItemBinding
import coursework.courseworkdb.SchoolEntity
import dagger.hilt.android.scopes.FragmentScoped
import okhttp3.internal.notify
import javax.inject.Inject

@FragmentScoped
class SchoolAdapter @Inject constructor(
    callback: SchoolDiffUtil
) : BaseRecyclerAdapter<SchoolEntity, ListItemBinding, SchoolViewHolder> (callback) {

    private var onDeleteListener: ((SchoolEntity) -> Unit)? = null
    fun setDeleteListener(listener: (SchoolEntity) -> Unit) {
        onDeleteListener = listener
    }

    private var onChangeListener: ((SchoolEntity) -> Unit)? = null
    fun setChangeListener(listener: (SchoolEntity) -> Unit) {
        onChangeListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SchoolViewHolder {
        return SchoolViewHolder(
            binding = ListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SchoolViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.binding.ibDelete.setOnClickListener {
            onDeleteListener?.invoke(getItem(holder.adapterPosition))
        }
        holder.binding.ibChange.setOnClickListener {
            onChangeListener?.invoke(getItem(holder.adapterPosition))
        }
    }
}