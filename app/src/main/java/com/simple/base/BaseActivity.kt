package com.simple.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.simple.tools.WindowUtil

abstract class BaseActivity : AppCompatActivity() {

    var topInset = 0
    var bottomInset = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowUtil.setTransparentStatusBar(window)
        WindowUtil.setLightStatus(window)
        if (layoutId() != 0) {
            setContentView(layoutId())
        }
        beforeView()
        initView(savedInstanceState)
        window.decorView.setOnApplyWindowInsetsListener { view, windowInsets ->
            topInset = windowInsets.systemWindowInsetTop
            bottomInset = windowInsets.systemWindowInsetBottom
            onInset()
            return@setOnApplyWindowInsetsListener view.onApplyWindowInsets(windowInsets)
        }
    }

    abstract fun layoutId(): Int
    abstract fun initView(savedInstanceState: Bundle?)

    open fun beforeView() {

    }

    open fun onInset() {

    }
}