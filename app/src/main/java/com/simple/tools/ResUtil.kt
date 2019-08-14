package com.simple.tools

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.TextAppearanceSpan
import androidx.annotation.*
import com.simple.R
import com.simple.base.MyApplication
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

object ResUtil {
    @JvmStatic
    fun dpToPx(dp: Float): Int {
        return (MyApplication.ctx.resources.displayMetrics.density * dp + 0.5).toInt()
    }
    @JvmStatic
    fun getString(@StringRes id: Int): String {
        return MyApplication.ctx.resources.getString(id)
    }
    @JvmStatic
    fun getString(@StringRes id:Int, vararg params:Any):String{
        return String.format(ResUtil.getString(id),*params)
    }

    @JvmStatic
    fun getStringArray(@ArrayRes arrayId:Int):Array<String>{
        return MyApplication.ctx.resources.getStringArray(arrayId)
    }
    @JvmStatic
    fun getIntArray(@ArrayRes arrayId: Int):IntArray{
        return MyApplication.ctx.resources.getIntArray(arrayId)
    }

    @JvmStatic
    fun getSize(@DimenRes id:Int):Int{
        return MyApplication.ctx.resources.getDimensionPixelSize(id)
    }

    @JvmStatic
    fun getSpannable(origin:String?, render:String?,@ColorInt color:Int,size:Int=-1):Spannable{
        var mSize=size
        if(size==-1) mSize=ResUtil.getSize(R.dimen.textSize_normal)
        val spannable= SpannableStringBuilder(origin)
        if(TextUtils.isEmpty(origin)||TextUtils.isEmpty(render))return spannable
        var index=origin!!.indexOf(render!!,0)
        while (index>=0){
            spannable.setSpan(TextAppearanceSpan("",0,mSize, ColorStateList.valueOf(color),null),
                    index,
                    index+render.length,
                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            index=origin.indexOf(render,index+1)
        }
        return spannable
    }

    /**
     * @param pattern mm:ss
     */
    @JvmStatic
    fun timeFormat(pattern: String, time: Long): String {
        val format = SimpleDateFormat(pattern, Locale.CHINA)
        return format.format(Date(time))
    }
    @JvmStatic
    fun getFileSize(size :Long):String{
        val format= DecimalFormat("0.00")
        return when(size){
            in 0..1024*1024-> format.format(size/1024F)+"KB"
            else->format.format(size/1024F/1024)+"MB"
        }
    }
    @JvmStatic
    fun getColor(@ColorRes colorId:Int):Int{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            MyApplication.ctx.resources.getColor(colorId,MyApplication.ctx.theme)
        } else {
            MyApplication.ctx.resources.getColor(colorId)
        }
    }

    @JvmStatic
    @ColorInt
    fun getColorReverse(@ColorInt color:Int):Int{
        return color xor 0xffffff
    }

    @JvmStatic
    fun getDrawable(@DrawableRes drawableId: Int):Drawable{
        return MyApplication.ctx.resources.getDrawable(drawableId,MyApplication.ctx.theme)
    }
    @JvmStatic
    fun getBitmapFromDrawable(drawable: Drawable):Bitmap{
        val bitmap=Bitmap.createBitmap(drawable.intrinsicWidth,drawable.intrinsicHeight,Bitmap.Config.ARGB_4444)
        val canvas=Canvas(bitmap)
        //********必须设置bounds
        drawable.setBounds(0,0,canvas.width,canvas.height)
        drawable.draw(canvas)
        return bitmap
    }
    @JvmStatic
    fun getBitmapFromResoucs(@DrawableRes drawableId: Int):Bitmap{
        return getBitmapFromDrawable(getDrawable(drawableId))
    }
    @JvmStatic
    fun bitmapOp(bitmap:Bitmap,matrix:Matrix):Bitmap{
        return Bitmap.createBitmap(bitmap,0,0,bitmap.width,bitmap.height,matrix,false)
    }
    @JvmStatic
    fun getBitmapHorizontalMirror(bitmap: Bitmap):Bitmap{
        val matrix=Matrix()
        matrix.postScale(-1f,1f)
        return bitmapOp(bitmap,matrix)
    }
    @JvmStatic
    fun getDrawableHorizontalMirror(@DrawableRes drawableId: Int):Bitmap{
        val mBitmap=getBitmapFromDrawable(getDrawable(drawableId))
        val res=getBitmapHorizontalMirror(mBitmap)
        mBitmap.recycle()
        return res
    }
    @JvmStatic
    fun getBitmapRotate(bitmap: Bitmap,degree:Float):Bitmap{
        val matrix=Matrix()
        matrix.postRotate(degree)
        return bitmapOp(bitmap,matrix)
    }
    @JvmStatic
    fun getBitmapRotate(@DrawableRes drawableId: Int, degree:Float=0f):Bitmap{
        val mBitmap= getBitmapFromDrawable(getDrawable(drawableId))
        val res= getBitmapRotate(mBitmap,degree)
        mBitmap.recycle()
        return res
    }
}