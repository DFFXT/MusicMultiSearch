package com.simple.base

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.tencent.mmkv.MMKV

class MyApplication :Application() {

    override fun onCreate() {
        super.onCreate()
        ctx=this
        MMKV.initialize(this)

        SmartRefreshLayout.setDefaultRefreshFooterCreator(DefaultRefreshFooterCreator{ctx,_->
            return@DefaultRefreshFooterCreator ClassicsFooter(ctx)
        })

    }

    companion object{
        @SuppressLint("StaticFieldLeak")
        @JvmStatic
        lateinit var ctx:Context
    }
}