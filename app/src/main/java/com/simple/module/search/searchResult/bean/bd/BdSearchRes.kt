package com.simple.module.search.searchResult.bean.bd

import com.google.gson.annotations.SerializedName

data class BdSearchRes<T>(val error_code: Int, var result: T)

data class BdSearchMusicResult(
    @SerializedName("song_info")
    val songInfo: BdSearchSongList
)

data class BdSearchSongList(
    @SerializedName("song_list")
    val songList: ArrayList<BdSearchMusic>? = arrayListOf(),
    @SerializedName("total")
    val total: Int
)

/**
 * 搜索音乐简单信息
 */
data class BdSearchMusic(
    @SerializedName("album_id")
    val albumId: String,
    @SerializedName("album_title")
    val albumTitle: String,
    @SerializedName("all_artist_id")
    val allArtistId: String,
    @SerializedName("all_rate")
    val allRate: String,
    @SerializedName("author")
    val author: String,
    @SerializedName("biaoshi")
    val quality: String? = null, //**"vip,lossless,perm-1" vip和无损
    @SerializedName("has_filmtv")
    val hasFilmTv: String? = null,
    @SerializedName("has_mv")
    val hasMV: Int,
    @SerializedName("lrclink")
    val lrcLink: String? = null,
    @SerializedName("pic_small")
    val picSmall: String?,
    @SerializedName("si_proxycompany")
    val siProxyCompany: String?,
    @SerializedName("song_id")
    val songId: String,
    @SerializedName("ting_uid")
    val uid: String,
    @SerializedName("all_artist_ting_uid")
    val allUid: String? = null,

    @SerializedName("title")
    val musicName: String,

    @SerializedName("album_image_url")
    val albumImage: String? = null,
    val info:String,
    @SerializedName("file_duration")
    val duration:String
)
