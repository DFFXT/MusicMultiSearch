package com.simple.module.internet

import com.simple.module.search.searchResult.bean.bd.BdMusicInfoList
import com.simple.module.search.searchResult.bean.bd.BdMusicInfoRes
import com.simple.module.search.searchResult.bean.bd.BdSearchMusicResult
import com.simple.module.search.searchResult.bean.bd.BdSearchRes
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface BdApis {
    @GET("http://musicapi.qianqian.com/v1/restserver/ting?from=android&version=6.9.1.0&channel=ppzs&operator=0&method=baidu.ting.search.merge&format=json&type=0&data_source=0&isNew=1&use_cluster=1")
    fun musicSearch(@Query("query") keyword: String,
                    @Query("page_size") pageSize: Int,
                    @Query("page_no") page: Int): Call<BdSearchRes<BdSearchMusicResult>>


    @GET("http://music.taihe.com/data/music/links?")
    fun musicInfo(@Query("songIds") songIds: String): Call<BdMusicInfoRes<BdMusicInfoList>>

    @GET("")
    fun requestLrc(@Url lrcPath:String):Call<String>

}