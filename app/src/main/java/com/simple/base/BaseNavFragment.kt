package com.simple.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

abstract class BaseNavFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        rootView.post {
            activity!!.window.decorView.requestApplyInsets()
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