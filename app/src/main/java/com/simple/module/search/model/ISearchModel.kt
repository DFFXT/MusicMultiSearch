package com.simple.module.search.model

import com.simple.bean.Lyrics
import com.simple.bean.SearchMusicRes
import com.simple.module.internet.Transform

interface ISearchModel {
    fun search(keyword: String, page: Int, pageSize: Int): Transform<SearchMusicRes>
    fun requestLrc(musicId: String):Transform<List<Lyrics>>
    fun requestPic(musicId: String):Transform<String>
    fun requestPath(musicId: String):Transform<String>
}