package com.example.filmssurf.base

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding

abstract class BaseRecyclerAdapter<Data, VB: ViewBinding, VH: BaseViewHolder<Data, VB>> (
    callback: DiffUtil.ItemCallback<Data>
) : ListAdapter<Data, VH>(callback){

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.item = getItem(position)
        holder.bind()
    }
}