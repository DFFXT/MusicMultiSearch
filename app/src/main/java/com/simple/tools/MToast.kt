package com.simple.tools

import android.graphics.Rect
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import com.simple.R
import com.simple.base.MyApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object MToast {
    @JvmStatic
    var mToast: Toast? = null

    @JvmStatic
    fun showToast(msg: String) {
        GlobalScope.launch(Dispatchers.Main) {
            val context = MyApplication.ctx
            if (mToast != null) {//**取消上次的Toast
                mToast?.cancel()
            }
            mToast = Toast(context)
            val view = LayoutInflater.from(context).inflate(R.layout.layout_toast, null, false) as TextView
            mToast?.view = view
            val drawable = context.resources.getDrawable(R.drawable.icon_waring_white, context.theme)
            drawable.bounds = Rect(0, 0, ResUtil.dpToPx(30F), ResUtil.dpToPx(30F))
            view.setCompoundDrawables(drawable, null, null, null)
            view.text = msg
            mToast?.setGravity(Gravity.TOP, 0, ResUtil.dpToPx(60f))
            mToast?.duration = Toast.LENGTH_SHORT
            mToast?.show()
        }
    }

    @JvmStatic
    fun showToast(@StringRes stringId: Int) {
        showToast(ResUtil.getString(stringId))
    }

}