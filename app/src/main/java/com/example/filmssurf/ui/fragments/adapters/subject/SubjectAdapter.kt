package com.example.filmssurf.ui.fragments.adapters.subject

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.filmssurf.base.BaseRecyclerAdapter
import com.example.filmssurf.databinding.ListItemBinding
import com.example.filmssurf.ui.fragments.adapters.student.StudentViewHolder
import coursework.courseworkdb.SchoolEntity
import coursework.courseworkdb.StudentEntity
import coursework.courseworkdb.SubjectEntity
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class SubjectAdapter @Inject constructor(
    callback: SubjectDiffUtil
) : BaseRecyclerAdapter<String, ListItemBinding, SubjectViewHolder> (callback) {

    private var onDeleteListener: ((SubjectEntity) -> Unit)? = null
    fun setDeleteListener(listener: (SubjectEntity) -> Unit) {
        onDeleteListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectViewHolder {
        return SubjectViewHolder(
            binding = ListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SubjectViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.binding.ibDelete.setOnClickListener {
            onDeleteListener?.invoke(SubjectEntity(getItem(holder.adapterPosition)))
        }
    }
}