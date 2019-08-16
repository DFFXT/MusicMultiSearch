package com.simple.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.LayoutRes
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

/**
 * 底部弹窗，具有生命周期
 * 需要在activity onDestroy处销毁
 */
abstract class BaseBottomSheet(ctx: Context, @LayoutRes layout:Int):BaseLifeCycle() {
    private val sheet = BottomSheetDialog(ctx)
    private var slideListener: ((View, Float) -> Unit)? = null
    private var stateChangeListener: ((View, Int) -> Unit)? = null
    protected val rootView= LayoutInflater.from(ctx).inflate(layout,null,false)!!

    init {
        sheet.setContentView(rootView)
        state=State.INITIALIZED
    }

    fun setPeekHeight(peekHeight: Int) {
        sheet.behavior.peekHeight = peekHeight
        sheet.behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                slideListener?.invoke(bottomSheet, slideOffset)
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                stateChangeListener?.invoke(bottomSheet, newState)
            }
        })
    }

    fun setOnSlideListener(listener: (View, Float) -> Unit) {
        slideListener = listener
    }

    fun setOnStateChangeListener(listener: (View, Int) -> Unit) {
        stateChangeListener = listener
    }

    open fun show() {
        state=State.RESUMED
        sheet.show()
    }

    open fun close() {
        sheet.dismiss()
    }
    open fun destory(){
        close()
        state=State.DESTROYED
    }
}