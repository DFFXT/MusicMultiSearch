package com.simple.module.search

import android.animation.ValueAnimator
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import androidx.core.animation.addListener
import com.simple.R
import com.simple.base.BaseActivity
import com.simple.base.BasePopupWindow
import com.simple.module.search.vm.Source
import com.simple.tools.WindowUtil
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : BaseActivity() {

    override fun layoutId() = R.layout.activity_search
    private val searchEngine = ArrayList<String>(4)

    init {
        searchEngine.add(Source.KG.value)
        searchEngine.add(Source.KW.value)
        searchEngine.add(Source.BD.value)
        searchEngine.add(Source.QQ.value)
    }

    override fun initView(savedInstanceState: Bundle?) {
        tv_searchMode.setOnClickListener {
            val pop = BasePopupWindow(this, layoutInflater.inflate(R.layout.layout_search_engine, null, false))
            pop.setBackground(ColorDrawable(0))
            pop.enableTouchDismiss(false) {
                val animator = ValueAnimator.ofInt(pop.rootView.bottom, pop.rootView.top)
                animator.duration = 1000L
                animator.addUpdateListener {value->
                    pop.rootView.bottom = value.animatedValue as Int
                }
                animator.addListener(onEnd = {
                    pop.dismiss()
                })
                animator.start()
            }
            pop.show(it, Gravity.START and Gravity.TOP, 0, WindowUtil.getStatusHeight() + view_line.bottom)
        }
    }

}