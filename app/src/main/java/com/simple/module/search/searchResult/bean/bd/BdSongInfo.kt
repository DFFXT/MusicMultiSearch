package com.simple.module.search.searchResult.bean.bd

import com.google.gson.annotations.SerializedName


data class BdSongInfo (
        @SerializedName("hot")
        val hot:Int,
        @SerializedName("title")
        val title:String,
        @SerializedName("country")
        val country:String,
        @SerializedName("ting_uid")
        val uid:String,
        @SerializedName("expire")
        val expire:Int=0,
        @SerializedName("si_proxycompany")
        val proxyCompany:String,
        @SerializedName("compose")
        val compose:String?=null,
        @SerializedName("artist_640_1136")
        val artistPic640x1136:String,
        @SerializedName("artist_500_500")
        val artistPic500x500:String,
        @SerializedName("artist_480_800")
        val artistPic480x800:String,
        @SerializedName("pic_big")
        val picBig:String,
        @SerializedName("album_id")
        val albumId:String,
        @SerializedName("album_title")
        val albumName:String,
        @SerializedName("total_listen_nums")
        val listenTimes:String,
        @SerializedName("song_id")
        val songId:String,
        @SerializedName("artist_id")
        val artistId:String,
        @SerializedName("artist")
        val artistName:String,
        @SerializedName("publishtime")
        val publishTime:String,
        @SerializedName("file_duration")
        val duration:String,
        @SerializedName("pic_radio")
        val picRadio:String,
        @SerializedName("lrclink")
        val lrcLink:String,
        @SerializedName("pic_small")
        val picSmall:String,
        @SerializedName("pic_premium")
        val picPremium:String

)