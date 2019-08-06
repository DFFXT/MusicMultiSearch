package com.simple.base

import androidx.annotation.LayoutRes

class BaseSingleAdapter<T>(@LayoutRes private val layout: Int, private val onBindView: (holder: BaseViewHolder, position: Int, item: T) -> Unit) : BaseAdapter<T>() {
    override fun getLayout(viewType: Int): Int = layout
    override fun onBindViewHolder(holder: BaseViewHolder, position: Int, item: T) {
        onBindView(holder, position, item)
    }
}