package com.simple.module.search.searchResult.bean.qq

import com.google.gson.annotations.SerializedName

data class QqSearchRes (
    val code:Int,
    val data:QqSearchData,
    val massage:String
)

data class QqSearchData(
    val song:QqSearchSong
)

data class QqSearchSong(
    val list:ArrayList<QqSearchMusic>,
    val totalnum:Int,
    val curpage:Int,
    val curnum:Int
)

data class QqSearchMusic(
    val id:String,
    val mid:String,
    val title:String,
    @SerializedName("subtitle")
    val subTitle: String,
    @SerializedName("interval")
    val duration:Int,
    val singer:ArrayList<QQASearchMusicAuthor>,
    val album:QqSearchAlbum
)
data class QqSearchAlbum(
    val name:String,
    @SerializedName("subtitle")
    val subTitle:String
)
data class QQASearchMusicAuthor(
    val name:String
)