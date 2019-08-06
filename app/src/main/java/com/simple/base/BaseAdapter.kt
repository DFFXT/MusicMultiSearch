package com.simple.base

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T> : RecyclerView.Adapter<BaseViewHolder>() {
    var data = ArrayList<T>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder(parent.inflate(getLayout(viewType)))
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        onBindViewHolder(holder, position, data[position])
    }

    fun update(data: List<T>) {
        if (this.data == data) return
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    fun addAll(data: List<T>) {
        if (data.isEmpty()) return
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    abstract fun onBindViewHolder(holder: BaseViewHolder, position: Int, item: T)
    @LayoutRes
    abstract fun getLayout(viewType: Int): Int
}


class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun setText(@IdRes id: Int, charSequence: CharSequence): TextView {
        val tv = itemView.findViewById<TextView>(id)
        tv.text = charSequence
        return tv
    }

    fun setImage(@IdRes id: Int, @DrawableRes drawableId: Int): ImageView {
        val iv = itemView.findViewById<ImageView>(id)
        iv.setImageResource(drawableId)
        return iv
    }
}