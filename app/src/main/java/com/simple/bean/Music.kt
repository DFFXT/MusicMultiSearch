package com.simple.bean

import com.simple.module.search.searchResult.vm.Source
import java.io.Serializable

data class Music(
        var musicId: String,
        var musicName: String,
        var albumName: String,
        var duration: Int,
        var artistName: String,
        var musicPath: String,
        var lrc: List<Lyrics>?,
        var iconPath: String,
        var source: Source?
):Serializable