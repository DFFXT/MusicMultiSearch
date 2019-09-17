package com.simple.module.main

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import com.simple.view.ImageDraw
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class LoadingDraw(private var speed: Int = 10,
                  private val strokeWidth: Int = 10,
                  private val circleRadius: Int = 10,
                  private val strokeColor: Int = Color.BLACK,
                  private val circleColor: Int = Color.BLUE) : ImageDraw {
    private var angle = 0f
    private val paint = Paint()
    private val rect = Rect()
    private val halfWidth = circleRadius
    private var deg = 0f

    init {
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = strokeWidth.toFloat()
        paint.color = Color.RED
    }

    override fun onAttach() {
        deg = 0f
        angle = 0f
    }

    override fun draw(v: View, canvas: Canvas) {
        canvas.getClipBounds(rect)
        //canvas.translate(v.paddingStart.toFloat(),v.paddingTop.toFloat())
        rect.set(rect.left + halfWidth, rect.top + halfWidth, rect.right - halfWidth, rect.bottom - halfWidth)
        paint.color = circleColor
        val r = rect.width() / 2f
        val rad = -PI * 2 * angle / 360
        canvas.drawCircle((r * cos(rad) + r).toFloat() + halfWidth+v.paddingStart, (r * sin(-rad) + r).toFloat() + halfWidth+v.paddingTop, circleRadius.toFloat(), paint)
        paint.color = strokeColor
        canvas.drawArc(rect.left.toFloat(), rect.top.toFloat(), rect.right.toFloat(), rect.bottom.toFloat(), 0f + deg, angle, false, paint)
        angle += speed
        if (angle == 360f) {
            deg += speed
        }
        if (deg > speed) deg += speed
        if (deg == 360f) deg = 0f

        v.invalidate()
    }
}