package com.example.filmssurf.ui.fragments.adapters.student

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.filmssurf.base.BaseRecyclerAdapter
import com.example.filmssurf.databinding.ListItemBinding
import coursework.courseworkdb.SchoolEntity
import coursework.courseworkdb.StudentEntity
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class StudentAdapter @Inject constructor(
    callback: StudentDiffUtil
) : BaseRecyclerAdapter<StudentEntity, ListItemBinding, StudentViewHolder> (callback) {

    private var onDeleteListener: ((StudentEntity) -> Unit)? = null
    fun setDeleteListener(listener: (StudentEntity) -> Unit) {
        onDeleteListener = listener
    }

    private var onChangeListener: ((StudentEntity) -> Unit)? = null
    fun setChangeListener(listener: (StudentEntity) -> Unit) {
        onChangeListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        return StudentViewHolder(
            binding = ListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.binding.ibDelete.setOnClickListener {
            onDeleteListener?.invoke(getItem(holder.adapterPosition))
        }
        holder.binding.ibChange.setOnClickListener {
            onChangeListener?.invoke(getItem(holder.adapterPosition))
        }
    }
}