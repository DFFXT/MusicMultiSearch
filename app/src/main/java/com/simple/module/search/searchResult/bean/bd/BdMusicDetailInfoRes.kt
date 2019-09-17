package com.simple.module.search.searchResult.bean.bd

import com.google.gson.annotations.SerializedName

data class BdMusicDetailInfoRes (
        @SerializedName("songinfo")
        val songInfo:BdSongInfo,
        @SerializedName("bitrate")
        val bitRate: BdBitRate
)