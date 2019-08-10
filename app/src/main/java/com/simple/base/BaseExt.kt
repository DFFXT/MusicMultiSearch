package com.simple.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout

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


fun TabLayout.combineWithViewPager2(vp2:ViewPager2,title:List<String>){
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