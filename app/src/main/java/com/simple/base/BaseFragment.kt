package com.simple.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    lateinit var rootView: View
    protected var init=false
    protected lateinit var mActivity: BaseActivity
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        rootView = inflater.inflate(layoutId(), container, false)
        initView()
        init=true
        return rootView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity=context as BaseActivity
    }


    @LayoutRes
    abstract fun layoutId(): Int

    abstract fun initView()

    companion object{
        const val REQUEST_PERMISSION_CODE=124
    }

}