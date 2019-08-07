package com.simple.tools

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.simple.base.MyApplication

object SoftInputUtil {
    fun closeInput(v:View){
        val input=MyApplication.ctx.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        input.hideSoftInputFromWindow(v.windowToken,0)
    }
}