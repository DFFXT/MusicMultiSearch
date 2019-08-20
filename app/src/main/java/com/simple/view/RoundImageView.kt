package com.simple.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.ImageView
import com.simple.R
import java.lang.Exception
import java.lang.RuntimeException

class RoundImageView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ImageView(context, attrs, defStyleAttr) {

    var beforeDraw:ImageDraw?=null
        set(value) {
            field=value
            value?.onAttach()
            postInvalidate()
        }
    var afterDraw:ImageDraw?=null
        set(value) {
            field=value
            value?.onAttach()
            postInvalidate()
        }
    var measureListener: IOnMeasure?=null

    var radius=0f
        set(value) {
            if(value==field)return
            field=value
            requestLayout()
        }
    var type=0
        set(value) {
            if(value==field)return
            field=value
            requestLayout()
        }
    init {
        if(attrs!=null){
            val at=context.obtainStyledAttributes(attrs, R.styleable.RoundImageView)
            radius=at.getDimension(R.styleable.RoundImageView_iv_radius,radius)
            type=at.getInt(R.styleable.RoundImageView_iv_type,type)
            val measure=at.getString(R.styleable.RoundImageView_measure)
            if(measure!=null){
                measureListener=Class.forName(measure).newInstance() as IOnMeasure
            }
            if(at.hasValue(R.styleable.RoundImageView_iv_afterDraw)){
                afterDraw=Class.forName(at.getString(R.styleable.RoundImageView_iv_afterDraw)?:throw Exception("iv_afterDraw need a class fullName")) as ImageDraw
            }
            at.recycle()
        }

    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        beforeDraw?.destroy()
        afterDraw?.destroy()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        beforeDraw?.onCreate()
        afterDraw?.onCreate()
    }


    private val path=Path()
    private val rect=RectF()
    override fun onDraw(canvas: Canvas) {
        beforeDraw?.draw(this,canvas)
        setPath()
        canvas.clipPath(path)
        super.onDraw(canvas)
        afterDraw?.draw(this,canvas)
    }

    private fun setPath(){
        path.reset()

        rect.set(paddingStart.toFloat(),paddingTop.toFloat(),width-paddingEnd.toFloat(),height-paddingBottom.toFloat())
        if(type==0){
            path.addRoundRect(rect,radius,radius,Path.Direction.CW)
        }else if (type==1){
            path.addRoundRect(rect,(width-paddingStart-paddingEnd)/2f,(height-paddingTop-paddingBottom)/2f,Path.Direction.CW)
        }

    }


    private val measure= IntArray(2)
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measure[0]=widthMeasureSpec
        measure[1]=heightMeasureSpec
        measureListener?.onMeasure(measure)
        super.onMeasure(measure[0], measure[1])
    }

}