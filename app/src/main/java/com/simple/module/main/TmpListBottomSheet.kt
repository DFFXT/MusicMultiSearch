package com.simple.module.main

import android.content.Context
import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.simple.R
import com.simple.base.BaseBottomSheet
import com.simple.base.BaseSingleAdapter
import com.simple.base.ifNullOrBlank
import com.simple.bean.Music
import com.simple.module.download.service.DownloadService
import com.simple.module.download.service.isInternetMusic
import com.simple.module.player.bean.PlayType
import com.simple.module.player.playerInterface.PlayerObserver
import com.simple.tools.ResUtil
import com.simple.tools.WindowUtil
import kotlinx.android.synthetic.main.layout_tmp_list.view.*

/**
 * 播放列表弹窗
 */
class TmpListBottomSheet(ctx: Context) : BaseBottomSheet(ctx, R.layout.layout_tmp_list) {
    private lateinit var playType: PlayType
    private lateinit var currentMusic: Music
    private val observer = object : PlayerObserver() {
        override fun onMusicLoad(music: Music) {
            currentMusic = music
        }

        override fun onListChange(list: List<Music>) {
            adapter.update(list)
        }

        override fun onPlayTypeChange(type: PlayType) {
            playType = type
            rootView.iv_playType.setImageResource(playType.drawable)
            rootView.tv_playType.text = playType.type
        }
    }
    private val adapter = BaseSingleAdapter<Music>(R.layout.item_tmp_list) { holder, position, item ->
        holder.setText(R.id.tv_musicName, item.musicName)
        holder.setText(R.id.tv_artistName, " - " + item.artistName.ifNullOrBlank(ResUtil.getString(R.string.unknownArtist)))
        holder.findView<View>(R.id.iv_playingFlag).visibility = View.GONE
        holder.findView<View>(R.id.iv_download).visibility = View.GONE
        holder.itemView.setBackgroundResource(R.drawable.selector_transparent_gray)
        if (item == currentMusic) {
            holder.itemView.setBackgroundColor(0xffeeeeee.toInt())
            holder.findView<View>(R.id.iv_playingFlag).visibility = View.VISIBLE
            if (item.isInternetMusic()) {
                val ivDownload=holder.findView<View>(R.id.iv_download)
                ivDownload.visibility = View.VISIBLE
                ivDownload.setOnClickListener{
                    DownloadService.addTask(ctx,item)
                    close()
                }
            }
            holder.itemView.setOnClickListener(null)
        }else{
            holder.itemView.setOnClickListener {
                ControllerActivity.op?.play(item)
                close()
            }
        }

        holder.findView<View>(R.id.iv_delete).setOnClickListener {
            ControllerActivity.op?.remove(position)
        }
    }

    init {
        rootView.iv_playType.setOnClickListener {
            ControllerActivity.op?.changePlayType(ControllerActivity.nextPlayType(playType))
        }
        rootView.rv_tmpList.layoutManager = LinearLayoutManager(ctx)
        rootView.rv_tmpList.adapter = adapter
        rootView.rv_tmpList.setHasFixedSize(true)
        val lp=rootView.layoutParams
        lp.height=WindowUtil.screenHeight()/2
        rootView.layoutParams=lp
        ControllerActivity.op?.addObserver(this, observer)
    }

    override fun show() {
        adapter.notifyDataSetChanged()
        rootView.rv_tmpList.scrollToPosition(ControllerActivity.op?.getIndex()?:0)
        super.show()
    }
}