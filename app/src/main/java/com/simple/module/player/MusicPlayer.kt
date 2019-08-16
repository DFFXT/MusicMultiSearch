package com.simple.module.player

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import androidx.lifecycle.LifecycleOwner
import com.simple.base.MyApplication
import com.simple.bean.Music
import com.simple.module.player.bean.PlayType
import com.simple.module.player.playerInterface.PlayerObserver
import com.simple.module.player.playerInterface.PlayerOperation
import com.simple.tools.MediaStoreUtil
import com.simple.tools.Ticker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class MusicPlayer : Service() {
    private val linkedList = LinkedListImp.load<Music>() ?: LinkedListImp(LinkedList())
    private val observerManager = PlayerObserverManager()
    private var playType = PlayType.ALL_CYCLE
    private var player: MediaPlayer = MediaPlayer()
    private var operationImp = PlayerOperationImp()
    private var playerPrepared = false
    private var autoPlay = false
    private val ticker = Ticker(500, 0, Dispatchers.Main) {
        observerManager.dispatchTimeChange(operationImp.getCurrentTime(), operationImp.getDuration())
    }

    init {
        MediaStoreUtil.queryFiles()
        MediaStoreUtil.queryAudio{
            linkedList.clear()
            linkedList.addAll(it)
        }

        player.setOnCompletionListener {
            observerManager.dispatchStatus(false)
            when (playType) {
                PlayType.ALL_CYCLE -> {
                    operationImp.next()
                }
                PlayType.ONE_CYCLE -> {
                    operationImp.start()
                    observerManager.dispatchStatus(true)
                }
                PlayType.RANDOM -> {
                    operationImp.random()
                }
            }
        }
        player.setOnPreparedListener {
            playerPrepared = true
            val music = linkedList.getCurrent()
            music.duration = operationImp.getDuration()
            observerManager.dispatchLoad(music)
            if (!autoPlay) {
                autoPlay = true
                return@setOnPreparedListener
            }
            operationImp.start()
        }
        player.setOnBufferingUpdateListener { _, i ->
            observerManager.dispatchBufferUpdate(i)
        }
        player.setOnErrorListener { _, _, _ ->
            ticker.stop()
            playerPrepared = false
            player.reset()
            return@setOnErrorListener true
        }

        if (linkedList.size > 0) {
            load(linkedList.getCurrent())
        } else {
            autoPlay = true
        }
    }

    override fun onBind(p0: Intent?): IBinder {
        return operationImp
    }

    inner class PlayerOperationImp : Binder(), PlayerOperation {
        override fun toggle() {
            if (!playerPrepared) return
            if (player.isPlaying) {
                operationImp.pause()
            } else {
                operationImp.start()
            }
        }

        override fun play(music: Music) {
            val index = linkedList.indexOf(music)
            if (index == -1) {
                linkedList.add(music.copy())
                load(linkedList.getLast(true))
            } else {
                linkedList.setIndex(index)
                load(linkedList.getCurrent())
            }

        }

        override fun next() {
            if (linkedList.size == 0) return
            load(linkedList.next())
        }

        override fun pre() {
            if (linkedList.size == 0) return
            load(linkedList.pre())
        }

        override fun random() {
            if (linkedList.size == 0) return
            load(linkedList.random())
        }

        override fun pause() {
            player.pause()
            observerManager.dispatchStatus(false)
        }

        override fun start() {
            player.start()
            ticker.start()
            observerManager.dispatchStatus(true)
        }

        override fun seekTo(time: Int) {
            player.seekTo(time)
        }

        override fun addToNext(music: Music) {
            linkedList.add(music, linkedList.getIndex() + 1)
            observerManager.dispatchListChange(linkedList)
        }

        override fun remove(index: Int) {
            if(linkedList.getIndex()==index){
                linkedList.remove(index)
                next()
            }else{
                linkedList.remove(index)
            }
            observerManager.dispatchListChange(linkedList)
        }

        override fun getCurrentTime(): Int = if (playerPrepared) player.currentPosition else 0

        override fun getDuration(): Int {
            return if (playerPrepared) player.duration else 0
        }

        override fun getPlayType() = playType

        override fun getMusicList() = linkedList

        override fun getIndex() = linkedList.getIndex()

        override fun changePlayType(playType: PlayType) {
            this@MusicPlayer.playType = playType
            observerManager.dispatchPlayType(playType)
        }

        override fun addObserver(lifecycleOwner: LifecycleOwner, observer: PlayerObserver) {
            observerManager.add(lifecycleOwner, observer)
            dispatchInfo(observer)
        }
    }

    private fun dispatchInfo(observer: PlayerObserver) {
        observer.onStatusChange(player.isPlaying,false)
        if (linkedList.size > 0) {
            observer.onMusicLoad(linkedList.getCurrent())
        }

        observer.onPlayTypeChange(playType)
        observer.onListChange(linkedList)
    }

    override fun onDestroy() {
        super.onDestroy()
        ticker.stop()
        player.release()
        linkedList.save()
    }

    private fun load(music: Music) {
        playerPrepared = false
        //observerManager.dispatchStatus(false)
        try {
            player.reset()
            player.setDataSource(music.musicPath)
            player.prepareAsync()
            observerManager.dispatchLoad(music)
        }catch (e:Exception){
            e.printStackTrace()
            operationImp.next()
        }

    }

}


