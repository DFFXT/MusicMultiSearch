package com.simple.module.internet

import com.simple.module.search.bean.qq.QqLyricsRes
import com.simple.module.search.bean.qq.QqMusicPathRes
import com.simple.module.search.bean.qq.QqSearchRes
import retrofit2.Call
import retrofit2.http.*

interface QqApis {
    @GET("https://c.y.qq.com/soso/fcgi-bin/client_search_cp?ct=24&qqmusic_ver=1298&new_json=1&remoteplace=txt.yqq.center&t=0&aggr=1&cr=1&catZhida=1&lossless=0&flag_qc=0&g_tk=5381&loginUin=0&hostUin=0&format=json&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq.json&needNewCode=0")
    fun search(@Query("w") keyword: String, @Query("p") page: Int, @Query("n") pageSize: Int): Call<QqSearchRes>

    @Headers("Referer: https://y.qq.com")
    @GET("https://c.y.qq.com/lyric/fcgi-bin/fcg_query_lyric_yqq.fcg?nobase64=1&-=jsonp1&g_tk=5381&loginUin=0&hostUin=0&format=json&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq.json&needNewCode=0")
    fun requestLrc(@Query("musicid") musicId: String):Call<QqLyricsRes>

    @GET("")
    fun requestKey(@Url url:String):Call<QqMusicPathRes>
}