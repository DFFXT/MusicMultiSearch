package com.simple.tools

import android.animation.Animator
import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import androidx.core.animation.addListener

object AnimatorUtil {
    @JvmStatic
    fun  animate(duration:Long,updateListener:ValueAnimator.AnimatorUpdateListener,vararg args:Number){
        val animator=ValueAnimator.ofObject(TypeEvaluator<Number>{fraction,start,end->
            start.toDouble()+(end.toDouble()-start.toDouble())*fraction
        },*args)
        animator.duration=duration
        animator.addUpdateListener(updateListener)
        animator.addListener {  }
        animator.start()
    }
}