package com.simple.module.search.searchResult

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.simple.R
import com.simple.base.*
import com.simple.bean.Music
import com.simple.module.download.service.DownloadService
import com.simple.module.main.vm.ControllerViewModel
import com.simple.module.player.playerInterface.PlayerOperation
import com.simple.module.search.related.SearchActivity
import com.simple.module.search.searchResult.vm.SearchViewModel
import com.simple.module.search.searchResult.vm.Source
import com.simple.tools.ResUtil
import kotlinx.android.synthetic.main.fragment_search_result.*
import kotlinx.android.synthetic.main.fragment_search_result.view.*

class SearchResultFragment : BaseNavFragment() {
    override var fitId = R.id.top
    override fun layoutId(): Int = R.layout.fragment_search_result
    private lateinit var keyword: String
    private val vm: SearchViewModel by lazy {
        ViewModelProviders.of(this)[SearchViewModel::class.java]
    }
    private val adapter = BaseSingleAdapter<Music>(R.layout.item_search_result) { holder, _, item ->
        holder.setText(R.id.tv_musicName, item.musicName)
        holder.setText(R.id.tv_artistName, item.artistName)
        if (item.albumName.isNotEmpty()) {
            holder.setText(R.id.tv_album, " - " + item.albumName)
        }
        holder.setText(R.id.tv_duration, ResUtil.timeFormat("mm:ss", item.duration.toLong()))
        holder.findView<View>(R.id.iv_info).setOnClickListener {
            dialog.show(item)
        }
        holder.itemView.setOnClickListener {

            vm.requestFull(item) {
                op?.play(item)
            }
        }
    }
    private var op: PlayerOperation? = null
    private val dialog: MusicOpBottomSheet by lazy {
        val data = ArrayList<MusicOpBottomSheet.MusicOp>(3)
        data.add((MusicOpBottomSheet.MusicOp(R.drawable.icon_pause_black, ResUtil.getString(R.string.addToNextPlay)) { v, item ->
            vm.livePath.observe(v.context as FragmentActivity, Observer { path ->
                item.musicPath = path
                op?.addToNext(item)
            })
            vm.requestPath(item.musicId)
            dialog.close()
        }))
        data.add(MusicOpBottomSheet.MusicOp(R.drawable.icon_kw, ResUtil.getString(R.string.download)) { v, item ->
            vm.requestFull(item) {
                DownloadService.addTask(v.context, item)
            }
            dialog.close()
        })
        data.add(MusicOpBottomSheet.MusicOp(R.drawable.icon_kw, ResUtil.getString(R.string.share)) { _, _ ->
            dialog.close()
        })
        MusicOpBottomSheet(activity!!, data, true)
    }


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
            rootView.refresh.showContent()
        })
        vm.status.observe(this, Observer {
            if (vm.page == 1) {
                rootView.refresh.showError()
            }
        })
        rootView.refresh.showLoading()
        vm.search(keyword)

        val cvm = ViewModelProviders.of(this)[ControllerViewModel::class.java]
        cvm.op.observe(context as FragmentActivity, Observer {
            op = it
        })
        cvm.connect(mActivity)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK) return
        if (requestCode == 200) {
            vm.source = Source.KW.defSource()
            rootView.refresh.showLoading()
            vm.search(data!!.getStringExtra(SearchActivity.DATA)!!)
        }
    }
}