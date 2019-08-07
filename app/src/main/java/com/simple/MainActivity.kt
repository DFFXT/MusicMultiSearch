package com.simple

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.simple.base.BaseActivity
import com.simple.module.internet.log
import com.simple.module.search.searchResult.vm.SearchViewModel
import com.simple.module.search.searchResult.vm.Source

class MainActivity : BaseActivity() {

    override fun layoutId() = R.layout.activity_main
    override fun initView(savedInstanceState: Bundle?) {
        val vm = ViewModelProviders.of(this)[SearchViewModel::class.java]
        vm.source = Source.QQ
        vm.liveSearch.observe(this, Observer {
            vm.requestLrc(it.data[0].musicId)
            vm.requestPic(it.data[0].musicId)
            vm.requestPath(it.data[0].musicId)
        })
        vm.liveLrc.observe(this, Observer {
            it.log()
        })
        vm.livePic.observe(this, Observer {
            it.log()
        })
        vm.livePath.observe(this, Observer {
            it.log()
        })
        vm.search("神话")

    }
}
