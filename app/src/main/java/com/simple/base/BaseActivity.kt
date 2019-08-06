package com.simple.base

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

abstract class BaseActivity : AppCompatActivity(), OnApplyWindowInsetsListener {

    var topInset = 0
    var bottomInset = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (layoutId() != 0) {
            setContentView(layoutId())
        }
        beforeView()
        initView(savedInstanceState)
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView, this)
    }

    abstract fun layoutId(): Int
    abstract fun initView(savedInstanceState: Bundle?)

    open fun beforeView() {

    }

    override fun onApplyWindowInsets(v: View, insets: WindowInsetsCompat): WindowInsetsCompat {
        topInset = insets.systemWindowInsetTop
        bottomInset = insets.systemWindowInsetBottom
        return ViewCompat.onApplyWindowInsets(v, insets)
    }
}