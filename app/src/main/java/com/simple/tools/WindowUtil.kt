package com.simple.tools

import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.readystatesoftware.systembartint.SystemBarTintManager
import com.simple.base.BaseActivity
import com.simple.base.MyApplication

object WindowUtil {
    @JvmStatic
    fun getStatusHeight(): Int {
        val resources = MyApplication.ctx.resources
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(resourceId)
    }

    @JvmStatic
    fun screenWidth(): Int {
        return MyApplication.ctx.resources.displayMetrics.widthPixels
    }

    @JvmStatic
    fun screenHeight(): Int {
        return MyApplication.ctx.resources.displayMetrics.heightPixels
    }

    @JvmStatic
    fun setTransparentStatusBar(window: Window) {
        window.decorView.systemUiVisibility =window.decorView.systemUiVisibility or  View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            window.statusBarColor = 0
            window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }
    fun setLightStatus(window: Window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = 0x30000000
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decorView = window.decorView
            decorView.systemUiVisibility = decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        }
    }

}