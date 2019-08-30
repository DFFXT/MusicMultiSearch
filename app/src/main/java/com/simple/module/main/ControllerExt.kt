package com.simple.module.main

import android.graphics.Bitmap
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import com.simple.R
import com.simple.bean.Lyrics
import com.simple.bean.Music
import com.simple.module.download.service.getAbsolutePicPath
import com.simple.module.lyrics.ui.LyricsActivity
import com.simple.module.player.MusicPlayer
import com.simple.module.player.bean.PlayType
import com.simple.module.player.playerInterface.PlayerObserver
import com.simple.module.player.playerInterface.PlayerOperation
import com.simple.tools.ImageLoad
import com.simple.tools.ResUtil
import com.simple.view.RoundImageView

fun ControllerActivity.Companion.nextPlayType(currentPlayType: PlayType): PlayType {
    return when (currentPlayType) {
        PlayType.ONE_CYCLE -> PlayType.ALL_CYCLE
        PlayType.ALL_CYCLE -> PlayType.RANDOM
        PlayType.RANDOM -> PlayType.ONE_CYCLE
    }
}

fun ControllerActivity.getObserver(playerOperation: PlayerOperation?): PlayerObserver {
    val ctx = this
    return object : PlayerObserver() {
        private val tmpListSheet: TmpListBottomSheet by lazy { TmpListBottomSheet(ctx, playerOperation) }
        private val bottomController = findViewById<ViewGroup>(R.id.bottom_controller)
        private val tvLeftTime = bottomController.findViewById<TextView>(R.id.tv_currentTime)
        private val tvRightTime = bottomController.findViewById<TextView>(R.id.tv_duration)
        private val tvMusicName = bottomController.findViewById<TextView>(R.id.tv_musicName)
        private val tvArtistName = bottomController.findViewById<TextView>(R.id.tv_artistName)
        private val ivArtistIcon = bottomController.findViewById<ImageView>(R.id.iv_singerIcon)
        private val ivPlayStatus = bottomController.findViewById<RoundImageView>(R.id.iv_pause)
        private val ivPlayType = bottomController.findViewById<ImageView>(R.id.iv_playType)
        private val seekBar = bottomController.findViewById<SeekBar>(R.id.bar)
        private var seekBarTouch = false

        init {
            ivArtistIcon.setOnClickListener {
                LyricsActivity.actionStart(it.context)
            }
            ivPlayStatus.setOnClickListener {
                playerOperation?.toggle()
            }
            bottomController.findViewById<ImageView>(R.id.iv_next).setOnClickListener {
                playerOperation?.next()
            }
            ivPlayType.setOnClickListener {
                playerOperation?.changePlayType(ControllerActivity.nextPlayType(playType))
            }
            bottomController.findViewById<ImageView>(R.id.iv_tmpList).setOnClickListener {
                tmpListSheet.show()
            }
            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    seekBarTouch = true
                }

                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    seekBarTouch = false
                    playerOperation?.seekTo(seekBar.progress)
                }
            })

        }

        override fun onMusicLoad(music: Music,bitmap: Bitmap?,lyrics: ArrayList<Lyrics>?) {
            tvMusicName.text = music.musicName
            tvArtistName.text = music.artistName
            tvLeftTime.setText(R.string.time_0)
            tvRightTime.text = ResUtil.timeFormat("mm:ss", music.duration.toLong())
            seekBar.max = music.duration
            when {
                bitmap!=null -> ivArtistIcon.setImageBitmap(bitmap)
                music.iconPath.isEmpty() -> ImageLoad.load(music.getAbsolutePicPath()).into(ivArtistIcon)
                else -> ImageLoad.load(music.iconPath).into(ivArtistIcon)
            }

        }

        override fun onTimeChange(time: Int, duration: Int) {
            tvLeftTime.text = ResUtil.timeFormat("mm:ss", time.toLong())
            if (seekBarTouch) return
            seekBar.progress = time

        }

        override fun onPlayTypeChange(type: PlayType) {
            playType = type
            ivPlayType.setImageResource(type.drawable)
        }

        override fun onStatusChange(isPlaying: Boolean, isLoading: Boolean) {
            ivPlayStatus.setImageResource(if (isPlaying || isLoading) R.drawable.icon_play_black else R.drawable.icon_pause_black)
        }
    }
}



