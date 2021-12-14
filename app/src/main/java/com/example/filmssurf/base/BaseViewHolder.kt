package com.example.filmssurf.base

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseViewHolder<Data, VB: ViewBinding> constructor(
    private val viewBinding: VB
) : RecyclerView.ViewHolder(viewBinding.root) {

    var item: Data? = null

    abstract fun bind()
}