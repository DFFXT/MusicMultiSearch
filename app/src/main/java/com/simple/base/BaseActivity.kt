package com.simple.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (layoutId() != 0) {
            setContentView(layoutId())
        }
        beforeView()
        initView(savedInstanceState)
    }

    abstract fun layoutId(): Int
    abstract fun initView(savedInstanceState: Bundle?)

    open fun beforeView() {

    }
}