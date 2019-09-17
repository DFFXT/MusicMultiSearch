package com.simple.module.search.searchResult

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.simple.R
import com.simple.base.BaseSingleAdapter
import com.simple.base.visibleOrGone
import com.simple.bean.Music
import com.simple.module.search.searchResult.vm.SearchViewModel
import com.simple.tools.ImageLoad
import kotlinx.android.synthetic.main.layout_music_bottom_op.view.*

class MusicOpBottomSheet(ctx: Context, data: List<MusicOp>, private val iconShow: Boolean = false) {

    private lateinit var music: Music

    private val bottomSheetView: View by lazy { LayoutInflater.from(ctx).inflate(R.layout.layout_music_bottom_op, null, false) }

    private val vm: SearchViewModel by lazy {
        val model = ViewModelProviders.of(ctx as FragmentActivity)[SearchViewModel::class.java]
        model.livePic.observe(ctx, Observer {
            loadImage(it)
        })
        return@lazy model
    }
    private val bottomSheet: BottomSheetDialog by lazy {
        val dialog = BottomSheetDialog(ctx)
        dialog.setContentView(bottomSheetView)
        bottomSheetView.rv_musicOp.layoutManager = LinearLayoutManager(ctx)
        val adapter = BaseSingleAdapter<MusicOp>(R.layout.item_music_op) { holder, _, item ->
            holder.setImage(R.id.iv_musicOpIcon, item.drawable)
            holder.setText(R.id.tv_musicOp, item.op)
            holder.itemView.setOnClickListener {
                item.click(it, music)
            }
        }
        adapter.addAll(data)
        bottomSheetView.rv_musicOp.adapter = adapter
        return@lazy dialog
    }

    private fun loadImage(url: String) {
        music.iconPath = url
        ImageLoad.load(url).into(bottomSheetView.iv_musicIcon)
    }

    fun show(music: Music) {
        bottomSheetView.iv_musicIcon.visibleOrGone(iconShow)
        this.music = music
        if (iconShow) {
            if (music.iconPath.isNotEmpty()) {
                loadImage(music.iconPath)
            } else {
                vm.requestPic(music.musicId)
            }
        }

        bottomSheetView.tv_musicName.text = music.musicName
        bottomSheetView.tv_artistName.text = music.artistName
        if (bottomSheet.isShowing) return
        bottomSheet.show()
    }

    fun close() {
        bottomSheet.dismiss()
    }

    data class MusicOp(val drawable: Int, val op: String, val click: (View, Music) -> Unit)

}