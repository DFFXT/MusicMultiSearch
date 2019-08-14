package com.simple.module.internet

import com.simple.module.search.searchResult.bean.kw.KwMusicInfoRes
import com.simple.module.search.searchResult.bean.kw.KwMusicPathRes
import com.simple.module.search.searchResult.bean.kw.KwSearchRes
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface KwApis {
    @GET("http://www.kuwo.cn/api/www/search/searchMusicBykeyWord")
    fun search(@Query("key") keyword: String, @Query("pn") page: Int, @Query("rn") size: Int): Call<KwSearchRes>

    @GET("http://m.kuwo.cn/newh5/singles/songinfoandlrc")
    fun requestInfo(@Query("musicId") musicId: String): Call<KwMusicInfoRes>

    @GET("http://www.kuwo.cn/url?format=mp3&response=url&type=convert_url3&br=256kmp3&from=web&t=1564648018451")
    fun requestPath(@Query("rid") musicId: String,@Query("t") time:Long):Call<KwMusicPathRes>

}