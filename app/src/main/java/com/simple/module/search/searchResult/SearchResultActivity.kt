package com.simple.module.search.searchResult

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.simple.R
import com.simple.base.BaseActivity
import com.simple.base.BaseSingleAdapter
import com.simple.bean.Music
import com.simple.module.search.searchResult.vm.SearchViewModel
import com.simple.module.search.searchResult.vm.Source
import com.simple.tools.ConfigIO
import kotlinx.android.synthetic.main.activity_search_result.*

class SearchResultActivity : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_search_result
    private lateinit var keyword:String
    private val vm: SearchViewModel by lazy {
        ViewModelProviders.of(this)[SearchViewModel::class.java]
    }
    private val adapter=BaseSingleAdapter<Music>(R.layout.item_search_result){holder,_,item->
        holder.setText(R.id.tv_name,item.musicName)
        holder.setText(R.id.tv_artist,item.artistName)
        if(item.albumName.isNotEmpty()){
            holder.setText(R.id.tv_album," - "+item.albumName)
        }

        holder.setText(R.id.tv_duration,item.duration)
    }

    override fun beforeView() {
        keyword=intent.getStringExtra("keyword")!!
    }
    override fun initView(savedInstanceState: Bundle?) {
        refresh.setOnLoadMoreListener {
            vm.search(keyword)
        }
        rv_searchResult.layoutManager=LinearLayoutManager(this)
        rv_searchResult.adapter=adapter
        vm.liveSearch.observe(this, Observer {
            adapter.update(it.data)
            refresh.finishLoadMore()
        })
        vm.search(keyword)
    }

    override fun onResume() {
        super.onResume()
        vm.source=Source.KW.defSource()
    }


    companion object {
        @JvmStatic
        fun actionStart(ctx: Context, keyword: String) {
            val intent = Intent(ctx, SearchResultActivity::class.java)
            intent.putExtra("keyword", keyword)
            ctx.startActivity(intent)
        }
    }
}