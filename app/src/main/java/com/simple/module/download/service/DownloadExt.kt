package com.simple.module.download.service

import com.simple.base.Constant
import com.simple.bean.Music
import com.simple.module.player.LinkedListImp
import java.io.File

fun Music.contentSame(music: Music): Boolean {
    if (this == music) return true
    return (this.source == music.source && this.musicId == music.musicId)
}

fun LinkedListImp<Music>.containMusic(music: Music): Boolean {
    for (m in this) {
        if (m.contentSame(music)) return true
    }
    return false
}

fun Music.getFileName(): String {
    return "$artistName - $musicName.mp3"
}

fun Music.getAbsolutePath(): String {
    return Constant.Storage.DOWNLOAD_PATH + File.separator + getFileName()
}