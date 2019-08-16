package com.simple.module.download.service

import com.simple.base.Constant
import com.simple.bean.Music
import com.simple.module.player.LinkedListImp
import java.io.File

fun Music.contentSame(music: Music): Boolean {
    if (this == music) return true
    return (this.source == music.source && this.musicId == music.musicId)
}

fun Music.isInternetMusic():Boolean{
    return (this.musicPath.contains("http"))
}

fun LinkedListImp<Music>.containMusic(music: Music): Boolean {
    for (m in this) {
        if (m.contentSame(music)) return true
    }
    return false
}

fun Music.getBaseName():String{
    return "$artistName - $musicName"
}

fun Music.getFileName(): String {
    return "${getBaseName()}.mp3"
}


fun Music.getAbsolutePath(): String {
    return Constant.Storage.DOWNLOAD_PATH + File.separator + getFileName()
}
fun Music.getAbsolutePicPath():String{
    return Constant.Storage.PIC_PATH + File.separator + getBaseName()+".png"
}
