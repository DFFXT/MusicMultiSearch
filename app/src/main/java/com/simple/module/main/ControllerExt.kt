package com.simple.module.main

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import com.simple.R
import com.simple.bean.Music
import com.simple.module.player.bean.PlayType
import com.simple.module.player.playerInterface.PlayerObserver
import com.simple.tools.ImageLoad
import com.simple.tools.ResUtil

fun ControllerActivity.Companion.nextPlayType(currentPlayType: PlayType): PlayType {
    return when (currentPlayType) {
        PlayType.ONE_CYCLE -> PlayType.ALL_CYCLE
        PlayType.ALL_CYCLE -> PlayType.RANDOM
        PlayType.RANDOM -> PlayType.ONE_CYCLE
    }
}

fun ControllerActivity.getObserver(): PlayerObserver {
    val ctx=this
    return object : PlayerObserver() {
        private val tmpListSheet:TmpListBottomSheet by lazy { TmpListBottomSheet(ctx) }
        private val bottomController = findViewById<ViewGroup>(R.id.bottom_controller)
        private val tvLeftTime = bottomController.findViewById<TextView>(R.id.tv_currentTime)
        private val tvRightTime = bottomController.findViewById<TextView>(R.id.tv_duration)
        private val tvMusicName = bottomController.findViewById<TextView>(R.id.tv_musicName)
        private val tvArtistName = bottomController.findViewById<TextView>(R.id.tv_artist)
        private val ivArtistIcon = bottomController.findViewById<ImageView>(R.id.iv_singerIcon)
        private val ivPlayStatus = bottomController.findViewById<ImageView>(R.id.iv_pause)
        private val ivPlayType = bottomController.findViewById<ImageView>(R.id.iv_playType)
        private val seekBar = bottomController.findViewById<SeekBar>(R.id.bar)
        private var seekBarTouch=false
        init {
            ivPlayStatus.setOnClickListener {
                ControllerActivity.op?.toggle()
            }
            bottomController.findViewById<ImageView>(R.id.iv_next).setOnClickListener {
                ControllerActivity.op?.next()
            }
            ivPlayType.setOnClickListener {
                ControllerActivity.op?.changePlayType(ControllerActivity.nextPlayType(playType))
            }
            bottomController.findViewById<ImageView>(R.id.iv_tmpList).setOnClickListener {
                tmpListSheet.show()
            }
            seekBar.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    seekBarTouch=true
                }

                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    seekBarTouch=false
                    ControllerActivity.op?.seekTo(seekBar.progress)
                }
            })

        }

        override fun onMusicLoad(music: Music) {
            tvMusicName.text = music.musicName
            tvArtistName.text = music.artistName
            tvLeftTime.setText(R.string.time_0)
            tvRightTime.text = ResUtil.timeFormat("mm:ss", music.duration.toLong())
            seekBar.max = music.duration
            ImageLoad.load(music.iconPath).into(ivArtistIcon)
        }

        override fun onTimeChange(time: Int, duration: Int) {
            tvLeftTime.text = ResUtil.timeFormat("mm:ss", time.toLong())
            if(seekBarTouch)return
            seekBar.progress = time

        }

        override fun onPlayTypeChange(type: PlayType) {
            playType = type
            ivPlayType.setImageResource(type.drawable)
        }

        override fun onStatusChange(isPlaying: Boolean) {
            ivPlayStatus.setImageResource(if (isPlaying) R.drawable.icon_play_black else R.drawable.icon_pause_black)
        }
    }
}



