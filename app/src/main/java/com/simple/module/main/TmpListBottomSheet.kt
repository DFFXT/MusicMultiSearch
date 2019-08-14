package com.simple.module.main

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import com.simple.R
import com.simple.base.BaseAdapter
import com.simple.base.BaseBottomSheet
import com.simple.base.BaseViewHolder
import com.simple.bean.Music
import com.simple.module.player.bean.PlayType
import com.simple.tools.ResUtil
import kotlinx.android.synthetic.main.layout_tmp_list.view.*

class TmpListBottomSheet(ctx: Context) : BaseBottomSheet(ctx, R.layout.layout_tmp_list) {
    private lateinit var playType: PlayType
    private val adapter = object : BaseAdapter<Music>() {
        override fun onBindViewHolder(holder: BaseViewHolder, position: Int, item: Music) {
            if (holder.type == R.layout.item_tmp_list) {
                holder.setText(R.id.tv_musicName, item.musicName)
                holder.setText(R.id.tv_artistName, item.artistName)
            }
            holder.itemView.setOnClickListener {
                ControllerActivity.op!!.play(item)
            }
        }

        override fun getLayout(viewType: Int): Int {
            return viewType
        }

        override fun getItemViewType(position: Int): Int {
            return if (position == ControllerActivity.op!!.getIndex()) R.layout.item_tmp_list else R.layout.item_tmp_list
        }
    }

    init {
        rooView.iv_playType.setOnClickListener {
            ControllerActivity.op?.changePlayType(ControllerActivity.nextPlayType(playType))
            playTypeChange()
        }
        rooView.rv_tmpList.layoutManager = LinearLayoutManager(ctx)
        rooView.rv_tmpList.adapter = adapter
    }

    private fun playTypeChange() {
        playType = ControllerActivity.op!!.getPlayType()
        rooView.iv_playType.setImageResource(playType.drawable)
        rooView.tv_playType.text = playType.type
    }

    override fun show() {
        playTypeChange()
        rooView.tv_musicCount.text = ResUtil.getString(R.string.musicCount, ControllerActivity.op!!.getMusicList().size)
        adapter.update(ControllerActivity.op!!.getMusicList())
        super.show()
    }

}