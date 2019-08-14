package com.simple.module.floatWindow

import android.content.Context
import android.os.Build
import android.view.*
import com.simple.R
import com.simple.base.MyApplication

class FlaotWindow private constructor() {
    private val rooView: View =
        LayoutInflater.from(MyApplication.ctx).inflate(R.layout.layout_float_window, null, false)
    private val windowManager = MyApplication.ctx.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private val lp =
        WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)

    init {

        if (Build.VERSION.SDK_INT >=24) {
            lp.type=WindowManager.LayoutParams.TYPE_PHONE
        }else{
            lp.type=WindowManager.LayoutParams.TYPE_TOAST
        }
        lp.gravity=Gravity.BOTTOM
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