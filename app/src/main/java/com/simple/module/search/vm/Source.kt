package com.simple.module.search.vm

import com.simple.R
import com.simple.tools.ResUtil

enum class Source(val value: String,val drawableId:Int) {

    KW(ResUtil.getString(R.string.searchEngine_KW),R.drawable.icon_kw),
    KG(ResUtil.getString(R.string.searchEngine_KG),R.drawable.icon_kg),
    BD(ResUtil.getString(R.string.searchEngine_BD),R.drawable.icon_bd),
    QQ(ResUtil.getString(R.string.searchEngine_QQ),R.drawable.icon_qq)
}
