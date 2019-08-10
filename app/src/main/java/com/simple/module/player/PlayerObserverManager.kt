package com.simple.module.player

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.simple.bean.Lyrics
import com.simple.bean.Music
import com.simple.module.player.playerInterface.PlayerObserver

class PlayerObserverManager {
    private val observerMap = HashMap<LifecycleOwner, PlayerObserver>()
    fun add(lifecycle: LifecycleOwner, observer: PlayerObserver) {
        observerMap[lifecycle] = observer
        lifecycle.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                remove(owner)
            }
        })
    }

    fun remove(lifecycle: LifecycleOwner) {
        observerMap.remove(lifecycle)
    }

    fun dispatchLoad(music: Music) {
        for (observer in observerMap) {
            observer.value.load(music)
        }
    }

    fun dispatchLyricsLoad(lyrics: List<Lyrics>) {
        for (observer in observerMap) {
            observer.value.lyricsLoad(lyrics)
        }
    }

    fun dispatchBufferUpdate(progress: Int) {
        for (observer in observerMap) {
            observer.value.bufferUpdate(progress)
        }
    }

    fun dispatchListChange(list: List<Music>) {
        for (observer in observerMap) {
            observer.value.listChange(list)
        }
    }
    fun dispatchStatus(isPlaying:Boolean){
        for (observer in observerMap) {
            observer.value.status(isPlaying)
        }
    }


    private class ObserverCompanion(val lifecycle: Lifecycle, observer: PlayerObserver)
}