package com.simple.base

import android.view.View

fun View.visibleOrInVisible(visible:Boolean){
    if(visible){
        this.visibility=View.VISIBLE
    }else{
        this.visibility=View.INVISIBLE
    }
}
fun View.visibleOrGone(visible:Boolean){
    if(visible){
        this.visibility=View.VISIBLE
    }else{
        this.visibility=View.GONE
    }
}