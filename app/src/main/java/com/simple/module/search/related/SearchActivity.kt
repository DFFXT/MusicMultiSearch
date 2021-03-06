package com.simple.module.search.related

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.core.animation.addListener
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.simple.R
import com.simple.base.BaseActivity
import com.simple.base.BaseAdapter
import com.simple.base.BasePopupWindow
import com.simple.base.BaseSingleAdapter
import com.simple.module.search.searchResult.vm.Source
import com.simple.tools.SoftInputUtil
import com.simple.tools.WindowUtil
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.layout_search_engine.view.*

class SearchActivity : BaseActivity(R.layout.activity_search) {

    private var animationRun = false
    private var duration = 200L
    private val searchEngine = Source.values().asList()
    private val searchEngineAdapter: BaseAdapter<Source> by lazy {
        return@lazy BaseSingleAdapter<Source>(R.layout.item_search_engine) { holder, _, item ->
            holder.setImage(R.id.iv_engineIcon, item.drawableId)
            holder.setText(R.id.tv_engineName, item.value)
            holder.itemView.setOnClickListener {
                Source.KW.changeDefSource(item)
                iv_currentSearchEngine.setImageResource(item.drawableId)
                hideSearchEngine()
            }
        }
    }
    private val popupWindow: BasePopupWindow by lazy {
        val pop = BasePopupWindow(this, R.layout.layout_search_engine, WindowUtil.screenWidth(), WindowUtil.screenHeight())
                .isFocusable(false)
        pop.rootView.setOnClickListener {
            hideSearchEngine()
        }
        pop.contentView.rv_searchEngine.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        pop.contentView.rv_searchEngine.adapter = searchEngineAdapter
        searchEngineAdapter.addAll(searchEngine)
        return@lazy pop
    }


    override fun initView(savedInstanceState: Bundle?) {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        searchView.text=intent.getStringExtra("keyword")?:""
        iv_currentSearchEngine.setOnClickListener {
            showSearchEnginePop()
        }
        iv_currentSearchEngine.setImageResource(Source.KW.defSource().drawableId)
        searchView.cancelCallback={
            searchView.clearFocus()
            SoftInputUtil.closeInput(it)
        }
        searchView.searchCallback={
            if(it.isNotEmpty()){
                SoftInputUtil.closeInput(searchView)
                val intent=Intent()
                intent.putExtra(DATA,it)
                setResult(Activity.RESULT_OK,intent)
                finish()
                //SearchResultActivity.actionStart(this,it)
            }
        }
    }

    private fun showSearchEnginePop() {
        if (animationRun) return
        animationRun = true
        popupWindow.showCenter(window.decorView)
        popupWindow.contentView.visibility = View.INVISIBLE
        popupWindow.contentView.post {
            popupWindow.rootView.setPaddingRelative(0, view_line.bottom-topInset, 0, 0)
            popupWindow.contentView.visibility = View.VISIBLE
            val y=-popupWindow.contentView.measuredHeight.toFloat()+topInset
            popupWindow.contentView.translationY = y
            val animator = ValueAnimator.ofFloat(y, 0f)
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
        const val DATA= "data"
        const val DEF_SEARCH_ENGINE_KEY = "DEF_SEARCH_ENGINE_KEY"
        @JvmStatic
        fun actionStart(ctx:FragmentActivity,keyword:String="",code:Int){
            val intent = Intent(ctx, SearchActivity::class.java)
            intent.putExtra("keyword", keyword)
            ctx.startActivityForResult(intent,code)
        }
    }

}