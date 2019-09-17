package com.simple.module.player

import android.graphics.Bitmap
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.simple.base.MyApplication
import com.simple.bean.Lyrics
import com.simple.bean.Music
import com.simple.module.player.bean.PlayType
import com.simple.module.player.playerInterface.PlayerObserver

class PlayerObserverManager {
    private val notification=MyNotification(MyApplication.ctx)
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

    fun dispatchLoad(music: Music,bitmap: Bitmap?,lyrics: ArrayList<Lyrics>?) {
        for (observer in observerMap) {
            observer.value.onMusicLoad(music,bitmap,lyrics)
        }
        notification.notifyLoadChange(music)
    }

    fun dispatchLyricsLoad(lyrics: List<Lyrics>) {
        for (observer in observerMap) {
            observer.value.onLyricsLoad(lyrics)
        }
    }

    fun dispatchBufferUpdate(progress: Int) {
        for (observer in observerMap) {
            observer.value.onBufferUpdate(progress)
        }
    }

    fun dispatchListChange(list: List<Music>) {
        for (observer in observerMap) {
            observer.value.onListChange(list)
        }
    }

    fun dispatchStatus(isPlaying: Boolean) {
        for (observer in observerMap) {
            observer.value.onStatusChange(isPlaying)
        }
        notification.notifyStatusChange(isPlaying)
    }

    fun dispatchPlayType(playType: PlayType) {
        for (observer in observerMap) {
            observer.value.onPlayTypeChange(playType)
        }
    }

    fun dispatchTimeChange(time: Int, duration: Int) {
        for (observer in observerMap) {
            observer.value.onTimeChange(time, duration)
        }
    }
}