package com.simple.module.search.searchResult

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.simple.R
import com.simple.base.BaseNavFragment
import com.simple.base.BaseSingleAdapter
import com.simple.bean.Music
import com.simple.module.main.ControllerActivity
import com.simple.module.search.related.SearchActivity
import com.simple.module.search.searchResult.vm.SearchViewModel
import com.simple.module.search.searchResult.vm.Source
import com.simple.tools.ResUtil
import kotlinx.android.synthetic.main.fragment_search_result.*
import kotlinx.android.synthetic.main.fragment_search_result.view.*

class SearchResultFragment : BaseNavFragment() {
    override fun layoutId(): Int = R.layout.fragment_search_result
    private lateinit var keyword: String
    private val vm: SearchViewModel by lazy {
        ViewModelProviders.of(this)[SearchViewModel::class.java]
    }
    private val adapter = BaseSingleAdapter<Music>(R.layout.item_search_result) { holder, _, item ->
        holder.setText(R.id.tv_name, item.musicName)
        holder.setText(R.id.tv_artist, item.artistName)
        if (item.albumName.isNotEmpty()) {
            holder.setText(R.id.tv_album, " - " + item.albumName)
        }
        holder.setText(R.id.tv_duration, ResUtil.timeFormat("mm:ss",item.duration.toLong()))
        holder.findView<View>(R.id.iv_info).setOnClickListener {
            dialog.show(item)
        }
        holder.itemView.setOnClickListener {

            vm.requestFull(item) {
                ControllerActivity.op?.play(item)
            }
        }
    }
    private val dialog: MusicOpBottomSheet by lazy { MusicOpBottomSheet(activity!!) }


    override fun initView() {
        keyword = arguments?.getString("keyword") ?: "null"
        rootView.tv_keyword.text = keyword
        rootView.tv_keyword.setOnClickListener {
            SearchActivity.actionStart(activity!!, code = 200)
        }
        rootView.iv_searchIcon.setOnClickListener {
            SearchActivity.actionStart(activity!!, keyword, 200)
        }
        rootView.iv_back.setOnClickListener {
            Navigation.findNavController(it).navigateUp()
        }
        rootView.refresh.setOnLoadMoreListener {
            vm.search(keyword)
        }
        rootView.rv_searchResult.layoutManager = LinearLayoutManager(context!!)
        rootView.rv_searchResult.adapter = adapter
        vm.liveSearch.observe(this, Observer {
            adapter.update(it.data)
            refresh.finishLoadMore()
        })
        vm.search(keyword)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK) return
        if (requestCode == 200) {
            vm.source = Source.KW.defSource()
            vm.search(data!!.getStringExtra(SearchActivity.DATA)!!)
        }
    }
}