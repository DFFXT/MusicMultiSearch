package com.simple.tools

import com.simple.base.MyApplication

object WindowUtil {
    @JvmStatic
    fun getStatusHeight():Int{
        val resources = MyApplication.ctx.resources
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(resourceId)
    }
    @JvmStatic
    fun screenWidth():Int{
        return MyApplication.ctx.resources.displayMetrics.widthPixels
    }
    @JvmStatic
    fun screenHeight():Int{
        return MyApplication.ctx.resources.displayMetrics.heightPixels
    }
}