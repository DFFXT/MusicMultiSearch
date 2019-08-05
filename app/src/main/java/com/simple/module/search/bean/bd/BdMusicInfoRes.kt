package com.simple.module.search.bean.bd

import com.google.gson.annotations.SerializedName

data class BdMusicInfoRes<T>(var data: T)

data class BdMusicInfoList(
    var songList: ArrayList<BdMusicInfo>
)

data class BdMusicInfo(

    @SerializedName("songId")
    var songId: String,
    @SerializedName("songName")
    var songName: String,
    @SerializedName("artistName")
    var artistName: String,
    var albumId: String? = null,
    var albumName: String? = null,
    @SerializedName("time")
    var duration: Int,
    @SerializedName("size")
    var size: Long,
    @SerializedName("lrcLink")
    var lrcLink: String? = null,
    @SerializedName("songLink")
    var songLink: String,
    @SerializedName("songPicSmall")
    var singerIconSmall: String? = null,
    @SerializedName("songPicBig")
    var singerIconBig: String?,
    var format: String
)