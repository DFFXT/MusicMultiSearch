package com.simple.module.player

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import androidx.lifecycle.LifecycleOwner
import com.simple.bean.Music
import com.simple.module.player.bean.PlayType
import com.simple.module.player.playerInterface.PlayerObserver
import com.simple.module.player.playerInterface.PlayerOperation
import java.util.*

class MusicPlayer : Service() {
    private val linkedList = LinkedListImp<Music>(LinkedList())
    private val observerManager = PlayerObserverManager()
    private var playType=PlayType.ALL_CYCLE
    private var player: MediaPlayer=MediaPlayer()
    private var operationImp = PlayerOperationImp()

    init {
        player.setOnCompletionListener {
            observerManager.dispatchStatus(false)
            when(playType){
                PlayType.ALL_CYCLE->{
                    operationImp.next()
                }
                PlayType.ONE_CYCLE->{
                    operationImp.start()
                    observerManager.dispatchStatus(true)
                }
                PlayType.RANDOM->{
                    operationImp.random()
                }
            }
        }
        player.setOnPreparedListener {
            operationImp.start()
            observerManager.dispatchStatus(true)
        }
        player.setOnBufferingUpdateListener { _, i ->
            observerManager.dispatchBufferUpdate(i)
        }

    }

    override fun onBind(p0: Intent?): IBinder {
        return operationImp
    }

    inner class PlayerOperationImp : Binder(), PlayerOperation {
        override fun next() {
            if (linkedList.size == 0) return
            load(linkedList.next())
        }

        override fun pre() {
            if (linkedList.size == 0) return
            load(linkedList.pre())
        }

        override fun random() {
            if(linkedList.size==0)return
            load(linkedList.random())
        }

        override fun pause() {
            player.pause()
        }

        override fun start() {
            player.start()
        }

        override fun getCurrentTime(): Int = player.currentPosition

        override fun addObserver(lifecycleOwner: LifecycleOwner, observer: PlayerObserver) {
            observerManager.add(lifecycleOwner, observer)
        }
    }

    private fun load(music: Music) {
        player.reset()
        player.setDataSource(music.musicPath)
        player.prepareAsync()
    }
}


