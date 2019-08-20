package com.simple.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.simple.module.internet.log

abstract class BaseNavFragment : BaseFragment() {
    abstract var fitId:Int
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        rootView.findViewById<View>(fitId)?.apply {
            setPaddingRelative(paddingStart,paddingTop+(activity as BaseActivity).topInset,paddingEnd,paddingBottom)
        }
        fragment = this
        return rootView
    }

    override fun onDestroy() {
        super.onDestroy()
        if (fragment == this) fragment = null
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @JvmStatic
        var fragment: BaseNavFragment? = null
    }
}