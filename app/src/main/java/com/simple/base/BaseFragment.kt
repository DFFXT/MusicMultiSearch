package com.simple.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    lateinit var rootView: View
    protected var init=false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        rootView = inflater.inflate(layoutId(), container, false)
        initView()
        init=true
        return rootView
    }


    @LayoutRes
    abstract fun layoutId(): Int

    abstract fun initView()

}