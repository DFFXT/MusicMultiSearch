package com.simple.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

fun View.visibleOrInVisible(visible: Boolean) {
    if (visible) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.INVISIBLE
    }
}

fun View.visibleOrGone(visible: Boolean) {
    if (visible) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}

fun ViewGroup.inflate(@LayoutRes layout: Int, attach: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layout, this, attach)
}