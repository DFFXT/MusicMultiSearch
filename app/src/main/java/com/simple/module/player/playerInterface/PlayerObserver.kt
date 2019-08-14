package com.simple.module.player.playerInterface

import com.simple.bean.Lyrics
import com.simple.bean.Music
import com.simple.module.player.bean.PlayType

abstract class PlayerObserver {
    open fun onMusicLoad(music: Music) {}
    open fun onLyricsLoad(lyrics: List<Lyrics>) {}
    open fun onBufferUpdate(progress: Int) {}
    open fun onListChange(list: List<Music>) {}
    open fun onStatusChange(isPlaying: Boolean) {}
    open fun onPlayTypeChange(type: PlayType) {}
    open fun onTimeChange(time: Int, duration: Int) {}
}