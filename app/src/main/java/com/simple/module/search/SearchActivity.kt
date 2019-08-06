package com.simple.module.search

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import androidx.core.animation.addListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.simple.R
import com.simple.base.BaseActivity
import com.simple.base.BaseAdapter
import com.simple.base.BasePopupWindow
import com.simple.base.BaseSingleAdapter
import com.simple.module.search.vm.Source
import com.simple.tools.ConfigIO
import com.simple.tools.WindowUtil
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.layout_search_engine.view.*

class SearchActivity : BaseActivity() {

    private var animationRun = false
    private var duration = 200L
    override fun layoutId() = R.layout.activity_search
    private val searchEngine = Source.values().asList()
    private val searchEngineAdapter: BaseAdapter<Source> by lazy {
        return@lazy BaseSingleAdapter<Source>(R.layout.item_search_engine) { holder, _, item ->
            holder.setImage(R.id.iv_engineIcon, item.drawableId)
            holder.setText(R.id.tv_engineName, item.value)
            holder.itemView.setOnClickListener {
                ConfigIO.put(DEF_SEARCH_ENGINE_KEY, item.name)
                iv_currentSearchEngine.setImageResource(item.drawableId)
                hideSearchEngine()
            }
        }
    }
    private val popupWindow: BasePopupWindow by lazy {
        val pop = BasePopupWindow(this, layoutInflater.inflate(R.layout.layout_search_engine, null, false), WindowUtil.screenWidth(), WindowUtil.screenHeight())
        pop.isFocusable(false)
        pop.rootView.setOnClickListener {
            hideSearchEngine()
        }
        pop.contentView.rv_searchEngine.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        pop.contentView.rv_searchEngine.adapter = searchEngineAdapter
        searchEngineAdapter.addAll(searchEngine)
        return@lazy pop
    }

    override fun initView(savedInstanceState: Bundle?) {
        iv_currentSearchEngine.setOnClickListener {
            showSearchEnginePop()
        }

        iv_currentSearchEngine.setImageResource(Source.valueOf(ConfigIO.getString(DEF_SEARCH_ENGINE_KEY, searchEngine[0].name)).drawableId)
    }

    private fun showSearchEnginePop() {
        if (animationRun) return
        animationRun = true
        popupWindow.showCenter(window.decorView)
        popupWindow.contentView.visibility = View.INVISIBLE
        popupWindow.contentView.post {
            popupWindow.rootView.setPaddingRelative(0, view_line.bottom, 0, 0)
            popupWindow.contentView.visibility = View.VISIBLE
            popupWindow.contentView.translationY = -popupWindow.contentView.measuredHeight.toFloat()
            val animator = ValueAnimator.ofFloat(-popupWindow.contentView.measuredHeight.toFloat(), 0f)
            animator.duration = duration
            animator.addUpdateListener {
                popupWindow.contentView.translationY = it.animatedValue as Float
            }
            animator.addListener(onEnd = {
                animationRun = false
            })
            animator.start()
        }
    }

    private fun hideSearchEngine() {
        if (animationRun) return
        val animator = ValueAnimator.ofFloat(0f, popupWindow.contentView.measuredHeight.toFloat())
        animator.duration = duration
        animator.addUpdateListener {
            popupWindow.contentView.translationY = -(it.animatedValue as Float)
        }
        animator.addListener(onEnd = {
            animationRun = false
            popupWindow.dismiss()
        })
        animationRun = true
        animator.start()
    }

    override fun onBackPressed() {
        if (popupWindow.isShow()) {
            hideSearchEngine()
            return
        }
        super.onBackPressed()
    }


    companion object {
        const val DEF_SEARCH_ENGINE_KEY = "DEF_SEARCH_ENGINE_KEY"
    }

}