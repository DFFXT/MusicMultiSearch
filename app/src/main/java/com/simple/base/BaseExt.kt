package com.simple.base

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.simple.R
import com.simple.tools.WindowUtil
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.IOException
import kotlin.math.max

fun View.visibleOrInVisible(visible: Boolean) {
    if (visible) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.INVISIBLE
    }
}

fun View.visibleOrGone(visible: Boolean) {
    if (visible) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}

fun ViewGroup.inflate(@LayoutRes layout: Int, attach: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layout, this, attach)
}


fun TabLayout.setUpWithViewPager2(vp2:ViewPager2, title:List<String>){
    title.forEach {
        addTab(newTab().setText(it))
    }
    this.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
        override fun onTabReselected(tab: TabLayout.Tab?) {

        }

        override fun onTabUnselected(tab: TabLayout.Tab) {

        }

        override fun onTabSelected(tab: TabLayout.Tab) {
            vp2.setCurrentItem(tab.position,true)
        }
    })
    vp2.registerOnPageChangeCallback(object:ViewPager2.OnPageChangeCallback(){
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

        }

        override fun onPageSelected(position: Int) {
            setScrollPosition(position,0f,false)
        }
    })
}


fun String?.ifNullOrBlank(candidateString:String):String{
    return if(this.isNullOrBlank()) candidateString else this
}
fun String?.ifNotNullOrBlank(candidateString:String):String?{
    return if(this.isNullOrBlank()) this else candidateString
}


fun Call.enqueue(ok:(ResponseBody?)->Unit, error:((IOException)->Unit)?=null){
    enqueue(object :Callback{
        override fun onFailure(call: Call, e: IOException) {
            error?.invoke(e)
        }

        override fun onResponse(call: Call, response: Response) {
            ok(response.body())
        }
    })
}


//**loading动画中的点击事件
var View.loadingClickListen: View.OnClickListener?
    get() {
        val listener=getTag(R.integer.loadingListenerId)
        return if(listener==null) null else listener as View.OnClickListener
    }
    set(v) {
        this.setTag(R.integer.loadingListenerId, v)
    }
//**loadingView
var View.loadingView: View?
    get() {
        val v = getTag(R.integer.loadingViewId)
        return if (v == null) null else v as View
    }
    set(v) {
        this.setTag(R.integer.loadingViewId, v)
    }
//**errorView
var View.errorView: View?
    get() {
        val v = getTag(R.integer.errorViewId)
        return if (v == null) null else v as View
    }
    set(v) {
        this.setTag(R.integer.errorViewId, v)
    }

var View.errorClickLinsten:View.OnClickListener?
    get() {
        val listener=getTag(R.integer.errorListenerId)
        return if(listener==null) null else listener as View.OnClickListener
    }
    set(v) {
        this.setTag(R.integer.errorListenerId, v)
    }
/**
 * 对一个view显示Loading动画，采用覆盖方式
 */
fun View.showLoading(isRootLoading: Boolean = false) {
    when {
        width > 0 -> mShowLoading()
        isRootLoading -> {
            mShowLoading(WindowUtil.screenWidth(), WindowUtil.screenHeight())
        }
        else -> {
            /*if(width!=ViewGroup.LayoutParams.WRAP_CONTENT&&
                    width!=ViewGroup.LayoutParams.MATCH_PARENT&&
                    height!=ViewGroup.LayoutParams.WRAP_CONTENT&&
                    height!=ViewGroup.LayoutParams.MATCH_PARENT){
                mShowLoading()
            }else*///{
                post {
                    mShowLoading()
                }
            //}

        }
    }
}



private fun View.mShowLoading(w: Int = 0, h: Int = 0) {
    hideError()
    var mLoadingView = loadingView
    if (mLoadingView == null) {
        mLoadingView = LayoutInflater.from(context).inflate(R.layout.layout_loading, null, false)
        Glide.with(this).asGif().load(R.drawable.loading_gif).into((mLoadingView as ViewGroup).findViewById(R.id.iv_loading))
        loadingView = mLoadingView
    }
    val p = parent as ViewGroup
    p.removeView(mLoadingView)//**防止连续调用addView
    p.addView(mLoadingView, max(width, w), max(height, h))
    mLoadingView.y = y - p.paddingTop
    mLoadingView.x = x - p.paddingStart
    mLoadingView.setOnClickListener { v ->
        loadingClickListen?.let {
            (it).onClick(v)
        }
    }
}


/**
 * 错误界面
 */
fun View.showError(tip:String?=null,drawable: Drawable?=null):ViewGroup {
    hideLoading()
    var mErrorView = errorView
    if (mErrorView == null) {
        mErrorView = LayoutInflater.from(context).inflate(R.layout.layout_error, null, false)
        //GlideApp.with(this).asGif().load(R.drawable.loading_gif).into((mErrorView as ViewGroup).findViewById(R.itemId.iv_refresh))
        errorView = mErrorView
        mErrorView as ViewGroup
    }
    if(tip!=null)
        mErrorView.findViewById<TextView>(R.id.tv_errorTip).text=tip
    if(drawable!=null)
        mErrorView.findViewById<ImageView>(R.id.iv_refresh).setImageDrawable(drawable)
    val p = parent as ViewGroup
    p.addView(mErrorView, width, height)
    mErrorView.y = y - p.paddingTop
    mErrorView.x = x - p.paddingStart
    mErrorView.setOnClickListener { v ->
        errorClickLinsten?.let {
            (it).onClick(v)
        }
    }
    return mErrorView as ViewGroup
}

/**
 * 隐藏loadingView动画
 */
fun View.hideLoading() {
    val mLoadingView = loadingView
    if (mLoadingView != null) {
        post{
            (parent as ViewGroup).removeView(mLoadingView)
        }

    }
}
fun View.hideError(){
    val mErrorView = errorView
    if(mErrorView!=null){
        post {
            (parent as ViewGroup).removeView(mErrorView)
        }
    }
}

/**
 * 还原内容界面
 */
fun View.showContent(){
    hideLoading()
    hideError()
}


