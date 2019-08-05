package com.simple.module.internet

import com.simple.module.search.bean.kg.KgMusicInfoRes
import com.simple.module.search.bean.kg.KgSearchRes
import com.simple.module.search.bean.kw.KwMusicInfoRes
import com.simple.module.search.bean.kw.KwSearchRes
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface KgApis {
    @GET("https://songsearch.kugou.com/song_search_v2?userid=-1&clientver=&platform=WebFilter&tag=em&filter=2&iscorrection=1&privilege_filter=0")
    fun search(@Query("keyword") keyword: String, @Query("page") page: Int, @Query("pagesize") size: Int): Call<KgSearchRes>

    /*@GET("http://m.kuwo.cn/newh5/singles/songinfoandlrc")
    fun requestInfo(@Query("musicId") musicId: String): Call<KwMusicInfoRes>*/

    @GET("https://wwwapi.kugou.com/yy/index.php?r=play/getdata")
    fun requestInfo(@Query("hash") musicId: String):Call<KgMusicInfoRes>


}