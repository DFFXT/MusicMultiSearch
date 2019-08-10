package com.simple.module.floatWindow

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import com.simple.R
import com.simple.base.MyApplication

class FlaotWindow private constructor() {
    private val rooView: View =
        LayoutInflater.from(MyApplication.ctx).inflate(R.layout.layout_search_engine, null, false)
    private val windowManager = MyApplication.ctx.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private val lp =
        WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)

    init {

    }

    fun show() {
        if (rooView.parent != null) return
        windowManager.addView(rooView, lp)
    }

    fun close() {
        windowManager.removeView(rooView)
    }

    companion object {
        @JvmStatic
        val window: FlaotWindow by lazy {
            FlaotWindow()
        }
    }

}