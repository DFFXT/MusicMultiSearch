package com.simple.module.search.vm

import com.simple.R
import com.simple.tools.ResUtil

enum class Source(val value: String) {
    KW(ResUtil.getString(R.string.searchEngine_KW)),
    KG(ResUtil.getString(R.string.searchEngine_KG)),
    BD(ResUtil.getString(R.string.searchEngine_BD)),
    QQ(ResUtil.getString(R.string.searchEngine_QQ))
}