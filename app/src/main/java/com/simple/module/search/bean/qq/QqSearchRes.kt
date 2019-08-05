package com.simple.module.search.bean.qq

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
    val singer:ArrayList<QQASearchMusicAuthor>
)
data class QQASearchMusicAuthor(
    val name:String
)