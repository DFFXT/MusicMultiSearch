package com.simple.tools

import com.simple.base.MyApplication

object WindowUtil {
    @JvmStatic
    fun getStatusHeight():Int{
        val resources = MyApplication.ctx.resources
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(resourceId)
    }
}