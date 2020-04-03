package com.simple.module.lyrics.ui

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.simple.R
import com.simple.base.BaseActivity
import com.simple.bean.Lyrics
import com.simple.bean.Music
import com.simple.bean.getLocalLyrics
import com.simple.module.main.vm.ControllerViewModel
import com.simple.module.player.bean.PlayType
import com.simple.module.player.playerInterface.PlayerObserver
import com.simple.module.player.playerInterface.PlayerOperation
import kotlinx.android.synthetic.main.activity_lyrics.*

class LyricsActivity : BaseActivity(R.layout.activity_lyrics) {
    private var op: PlayerOperation? = null

    private val observer = object : PlayerObserver() {
        override fun onMusicLoad(music: Music, bitmap: Bitmap?, lyrics: ArrayList<Lyrics>?) {
            if (lyrics != null) {
                lyricsView.lyrics = lyrics
                lyricsView.setCurrentTimeImmediately(op!!.getCurrentTime())
            } else {
                music.getLocalLyrics {
                    lyricsView.lyrics = it
                    lyricsView.setCurrentTimeImmediately(op!!.getCurrentTime())
                }
            }
        }

        override fun onLyricsLoad(lyrics: List<Lyrics>) {
            lyricsView.lyrics = lyrics
        }

        override fun onTimeChange(time: Int, duration: Int) {
            lyricsView.setCurrentTime(time)
        }

        override fun onStatusChange(isPlaying: Boolean, isLoading: Boolean) {
            super.onStatusChange(isPlaying, isLoading)
        }

        override fun onPlayTypeChange(type: PlayType) {
            super.onPlayTypeChange(type)
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        val vm = ViewModelProviders.of(this)[ControllerViewModel::class.java]
        vm.op.observe(this, Observer {
            op = it
            it?.addObserver(this@LyricsActivity, observer)
        })
        vm.connect(this)

        lyricsView.setSeekListener {
            op?.seekTo(it)
            return@setSeekListener true
        }
    }

    companion object {
        @JvmStatic
        fun actionStart(ctx: Context) {
            ctx.startActivity(Intent(ctx, LyricsActivity::class.java))
        }
    }
}