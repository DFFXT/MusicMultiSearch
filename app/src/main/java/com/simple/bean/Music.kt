package com.simple.bean

import android.net.Uri
import com.simple.base.Constant
import com.simple.base.replaceLast
import com.simple.module.search.searchResult.vm.Source
import com.simple.tools.IOUtil
import com.simple.tools.LyricsAnalysis
import com.simple.tools.MediaStoreUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
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
) : Serializable {
    var localDisplayName: String? = null
}

fun Music.getLocalLyrics(callback: (List<Lyrics>?) -> Unit) {
    GlobalScope.launch(Dispatchers.IO) {
        val path = getLyricsPath() ?: return@launch
        IOUtil.readText(FileInputStream(path)) { str ->
            GlobalScope.launch(Dispatchers.Main) {
                callback(LyricsAnalysis(str).lyricsList)
            }
        }
    }

}

fun Music.getLyricsUri(): Uri? {
    val path = getLyricsPath() ?: return null
    val id = MediaStoreUtil.queryExist(path)
    return if (id > 0) MediaStoreUtil.getFileUri(id.toString()) else null
}

fun Music.getLyricsPath(): String? {
    if (localDisplayName == null) return null
    val suffix = MediaStoreUtil.getSuffix(localDisplayName!!)
    return Constant.Storage.LYRICS_PATH + File.separator + localDisplayName!!.replaceLast(
        suffix!!,
        ".lrc"
    )
}


fun Music.getBaseName(): String {
    return "$artistName - $musicName"
}

fun Music.getFileName(): String {
    return "${getBaseName()}.mp3"
}


fun Music.getAbsolutePath(): String {
    return Constant.Storage.DOWNLOAD_PATH + File.separator + getFileName()
}

fun Music.getAbsolutePicPath(): String {
    return Constant.Storage.PIC_PATH + File.separator + getBaseName() + ".png"
}