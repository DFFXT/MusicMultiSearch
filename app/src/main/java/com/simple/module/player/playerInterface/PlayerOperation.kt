package com.simple.module.player.playerInterface

import androidx.lifecycle.LifecycleOwner
import com.simple.bean.Music
import com.simple.module.player.bean.PlayType
import java.io.Serializable

interface PlayerOperation {
    fun toggle()
    fun play(music: Music)
    fun next()
    fun pre()
    fun random()
    fun pause()
    fun start()
    fun seekTo(time: Int)
    fun addToNext(music: Music)
    fun getCurrentTime(): Int
    fun getDuration(): Int
    fun getPlayType(): PlayType
    fun getMusicList():List<Music>
    fun getIndex():Int
    fun changePlayType(playType: PlayType)
    fun addObserver(lifecycleOwner: LifecycleOwner, observer: PlayerObserver)
}

interface LinkedListI<T> : List<T>, Serializable {
    fun next(): T
    fun pre(): T
    fun getCurrent(): T
    fun random(): T
    fun getIndex(): Int
    fun reset()
}