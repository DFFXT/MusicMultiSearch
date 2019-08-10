package com.simple.module.player.playerInterface

import androidx.lifecycle.LifecycleOwner

interface PlayerOperation {
    fun next()
    fun pre()
    fun random()
    fun pause()
    fun start()
    fun getCurrentTime():Int
    fun addObserver(lifecycleOwner: LifecycleOwner,observer: PlayerObserver)
}
interface LinkedListI<T>:List<T>{
    fun next():T
    fun pre():T
    fun random():T
}