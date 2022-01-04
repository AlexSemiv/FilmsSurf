package com.example.filmssurf.ui.fragments.adapters.student

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.example.courseworkdb.data.Student
import com.example.filmssurf.R
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

    private var onAddSubjectsListener: ((StudentEntity) -> Unit)? = null
    fun setAddSubjectsListener(listener: (StudentEntity) -> Unit) {
        onAddSubjectsListener = listener
    }

    private var onDeleteListener: ((StudentEntity) -> Unit)? = null
    fun setDeleteListener(listener: (StudentEntity) -> Unit) {
        onDeleteListener = listener
    }

    private var onChangeListener: ((StudentEntity) -> Unit)? = null
    fun setChangeListener(listener: (StudentEntity) -> Unit) {
        onChangeListener = listener
    }

    private var shouldShowActions: Boolean? = null
    fun setShouldShowActions(shouldShow: Boolean = true) {
        shouldShowActions = shouldShow
    }

    private var onShowMoreListener: ((StudentEntity) -> Unit)? = null
    fun setShowMoreListener(listener: (StudentEntity) -> Unit) {
        onShowMoreListener = listener
    }

    private var showMoreIcon: Int? = null
    fun setShowMoreIcon(icon: Int) {
        showMoreIcon = icon
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
        if(shouldShowActions == false) {
            holder.binding.apply {
                ibChange.isVisible = false
                ibShowMore.isVisible = false
                ibDelete.isVisible = false
            }
        } else {
            holder.binding.ibAdd.apply {
                setImageResource(R.drawable.ic_add_subject)
                isVisible = true
                setOnClickListener {
                    onAddSubjectsListener?.invoke(getItem(holder.adapterPosition))
                }
            }
            holder.binding.ibDelete.setOnClickListener {
                onDeleteListener?.invoke(getItem(holder.adapterPosition))
            }
            holder.binding.ibChange.setOnClickListener {
                onChangeListener?.invoke(getItem(holder.adapterPosition))
            }
            holder.binding.ibShowMore.apply {
                showMoreIcon?.let {
                    setImageResource(it)
                }
                setOnClickListener {
                    onShowMoreListener?.invoke(getItem(holder.adapterPosition))
                }
            }
        }
    }
}