package com.simple.module.download.service

import com.simple.bean.Music
import com.simple.module.player.LinkedListImp

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

