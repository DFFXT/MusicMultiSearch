package com.simple.module.player.playerInterface

import com.simple.bean.Lyrics
import com.simple.bean.Music

abstract class PlayerObserver{
    open fun load(music:Music){}
    open fun lyricsLoad(lyrics:List<Lyrics>){}
    open fun bufferUpdate(progress:Int){}
    open fun listChange(list:List<Music>){}
    open fun status(isPlaying:Boolean){}
}