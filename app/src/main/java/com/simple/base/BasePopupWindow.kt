package com.simple.base

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.*
import android.widget.PopupWindow
import com.simple.view.MaxSizeOnMeasure
import com.simple.view.MyViewGroup

open class BasePopupWindow @JvmOverloads constructor (
        private val ctx: Context,
        val layout: Int,
        width:Int=ViewGroup.LayoutParams.WRAP_CONTENT,
        height:Int=ViewGroup.LayoutParams.WRAP_CONTENT,
        maxWidth:Int=ViewGroup.LayoutParams.WRAP_CONTENT,
        maxHeight:Int=ViewGroup.LayoutParams.WRAP_CONTENT) {

    private val popupWindow: PopupWindow = PopupWindow(width, height)
    private var enableWindowDark=true
    var dismissCallback:(()->Unit)?=null
    val rootView= MyViewGroup(ctx)
    val contentView:View
    init {
        contentView=rootView.inflate(layout,false)
        rootView.addView(contentView)
        rootView.measureListener= MaxSizeOnMeasure(maxWidth,maxHeight)
        popupWindow.contentView = rootView
        setBackground(ColorDrawable(Color.TRANSPARENT))
        popupWindow.isOutsideTouchable = false
        popupWindow.isFocusable = true

        popupWindow.setOnDismissListener {
            //applyWindowDarkAlpha(0.5f, 1f, 300)
            dismissCallback?.invoke()
        }
    }

    fun enableWindowDark(enable:Boolean){
        enableWindowDark=enable
    }
    fun isFocusable(focusable:Boolean):BasePopupWindow{
        popupWindow.isFocusable=focusable
        return this
    }

    fun enableTouchDismiss(touchDismiss: Boolean,outTouchCallback:(()->Unit)?=null):BasePopupWindow{
        if (!touchDismiss) {
            popupWindow.isFocusable = true
            popupWindow.isOutsideTouchable = false

            contentView.isFocusable = true
            contentView.isFocusableInTouchMode = true
            contentView.setOnKeyListener { _, keyCode, _ ->
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dismiss()
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false
            }
            popupWindow.setTouchInterceptor(View.OnTouchListener { _, event ->
                val x = event.x.toInt()
                val y = event.y.toInt()
                if (event.action == MotionEvent.ACTION_DOWN && (x < 0 || x >= contentView.width || y < 0 || y >= contentView.height)) {
                    outTouchCallback?.invoke()
                    return@OnTouchListener true
                } else if (event.action == MotionEvent.ACTION_OUTSIDE) {
                    outTouchCallback?.invoke()
                    return@OnTouchListener true
                }
                false
            })
        }
        return this
    }

    private fun applyWindowDarkAlpha(from: Float, to: Float, duration: Int) {
        if(!enableWindowDark)return
        val window = (ctx as Activity).window
        val lp = window.attributes
        val animator = ValueAnimator.ofFloat(from, to)
        animator.duration = duration.toLong()
        animator.addUpdateListener { animation ->
            lp.alpha = animation.animatedValue as Float
            window.attributes = lp
        }
        animator.start()
    }
    fun setClipingEnable(enable: Boolean):BasePopupWindow{
        popupWindow.isClippingEnabled=enable
        return this
    }

    fun setBackground(drawable: Drawable) {
        popupWindow.setBackgroundDrawable(drawable)
    }

    fun isShow():Boolean{
        return popupWindow.isShowing
    }


    @JvmOverloads
    fun show(parent: View, gravity: Int=Gravity.CENTER, x: Int=0, y: Int=0) {
        popupWindow.update()
        popupWindow.showAtLocation(parent, gravity, x, y)
        //applyWindowDarkAlpha(1f, 0.5f, 300)
    }
    open fun showCenter(parent: View){
        show(parent,Gravity.CENTER,0,0)
    }

    open fun dismiss() {
        popupWindow.dismiss()
    }

}