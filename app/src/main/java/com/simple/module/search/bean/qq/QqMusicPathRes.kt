package com.simple.module.search.bean.qq

import com.google.gson.annotations.SerializedName

data class QqMusicPathRes(
    @SerializedName("req_0")
    val req0: QqReq0
)


data class QqReq0(
    val data:QqMusicPathInnerData
)

data class QqMusicPathInnerData(
    val midurlinfo:ArrayList<QqMusicPath>
)

/*data class MidUrlInfo(
    @SerializedName("0")
    val pathInfo:
)*/

data class QqMusicPath(
    val purl:String,
    val vkey:String
)