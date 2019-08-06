package com.simple.base

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.tencent.mmkv.MMKV

class MyApplication :Application() {

    override fun onCreate() {
        super.onCreate()
        ctx=this
        MMKV.initialize(this)
    }

    companion object{
        @SuppressLint("StaticFieldLeak")
        @JvmStatic
        lateinit var ctx:Context
    }
}