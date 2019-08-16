package com.simple.module.main

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import com.simple.R
import com.simple.base.BaseNavFragment
import com.simple.module.search.related.SearchActivity
import kotlinx.android.synthetic.main.fragment_local_music.view.*

class MainFragment : BaseNavFragment() {
    override fun layoutId() = R.layout.fragment_local_music

    lateinit var v: View
    override fun initView() {
        rootView.tv_test.setOnClickListener {
            v = it
            SearchActivity.actionStart(context!! as FragmentActivity, code = 200)
        }
        rootView.iv_test.afterDraw=LoadingDraw()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != RESULT_OK) return
        if (requestCode == 200) {
            v.post {
                val b = Bundle()
                b.putString("keyword", data!!.getStringExtra(SearchActivity.DATA))
                Navigation.findNavController(v).navigate(R.id.action_gotToSearchResult, b)
            }
        }
    }
}