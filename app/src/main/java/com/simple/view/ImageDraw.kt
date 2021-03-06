package com.simple.view

import android.graphics.Canvas
import android.view.View

interface ImageDraw {
    fun onAttach(){}
    fun onCreate(){}
    fun draw(v: View, canvas: Canvas)
    fun destroy(){}
}