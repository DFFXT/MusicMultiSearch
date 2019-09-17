package com.simple.module.search.searchResult.bean.bd

import com.google.gson.annotations.SerializedName

data class BdBitRate(
        @SerializedName("file_link")
        val songLink:String,
        @SerializedName("file_size")
        val fileSize:Long,
        @SerializedName("file_extension")
        val format:String,
        @SerializedName("file_duration")
        val duration:Int,
        @SerializedName("file_bitrate")
        val bitrate:Int
)