package com.example.filmssurf.ui.fragments.adapters.subject

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.example.filmssurf.R
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

    private var onAddSubjectsListener: ((SubjectEntity) -> Unit)? = null
    fun setAddSubjectsListener(listener: (SubjectEntity) -> Unit) {
        onAddSubjectsListener = listener
    }

    private var onDeleteListener: ((SubjectEntity) -> Unit)? = null
    fun setDeleteListener(listener: (SubjectEntity) -> Unit) {
        onDeleteListener = listener
    }

    private var shouldShowActions: Boolean? = null
    fun setShouldShowActions(shouldShow: Boolean = true) {
        shouldShowActions = shouldShow
    }

    private var onShowMoreListener: ((SubjectEntity) -> Unit)? = null
    fun setShowMoreListener(listener: (SubjectEntity) -> Unit) {
        onShowMoreListener = listener
    }

    private var showMoreIcon: Int? = null
    fun setShowMoreIcon(icon: Int) {
        showMoreIcon = icon
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
        holder.binding.ibChange.isVisible = false
        if(shouldShowActions == false) {
            holder.binding.apply {
                ibDelete.isVisible = false
                ibShowMore.isVisible = false
            }
        } else {
            holder.binding.ibAdd.apply {
                setImageResource(R.drawable.ic_add_student)
                isVisible = true
                setOnClickListener {
                    onAddSubjectsListener?.invoke(SubjectEntity(getItem(holder.adapterPosition)))
                }
            }
            holder.binding.ibDelete.setOnClickListener {
                onDeleteListener?.invoke(SubjectEntity(getItem(holder.adapterPosition)))
            }
            holder.binding.ibShowMore.setOnClickListener {

            }
            holder.binding.ibShowMore.apply {
                showMoreIcon?.let {
                    setImageResource(it)
                }
                setOnClickListener {
                    onShowMoreListener?.invoke(SubjectEntity(getItem(holder.adapterPosition)))
                }
            }
        }
    }
}