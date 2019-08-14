package com.simple.module.search.searchResult

import android.content.Context
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.simple.R
import com.simple.base.BaseSingleAdapter
import com.simple.bean.Music
import com.simple.module.download.DownloadActivity
import com.simple.module.download.service.DownloadService
import com.simple.module.main.ControllerActivity
import com.simple.module.search.searchResult.vm.SearchViewModel
import com.simple.tools.IOUtil
import com.simple.tools.ImageLoad
import com.simple.tools.MediaStoreUtil
import com.simple.tools.ResUtil
import kotlinx.android.synthetic.main.layout_music_bottom_op.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.URL

class MusicOpBottomSheet(ctx: Context) {

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
            holder.itemView.setOnClickListener(item.click)
        }
        val data = ArrayList<MusicOp>()
        data.add((MusicOp(R.drawable.icon_pause_black,ResUtil.getString(R.string.addToNextPlay),View.OnClickListener {
            vm.livePath.observe(it.context as FragmentActivity, Observer {path->
                music.musicPath=path
                ControllerActivity.op?.addToNext(music)
            })
            vm.requestPath(music.musicId)

        })))
        data.add(MusicOp(R.drawable.icon_kw, ResUtil.getString(R.string.download), View.OnClickListener {
            vm.requestFull(music){
                DownloadService.addTask(ctx,music)
            }
            close()
        }))
        data.add(MusicOp(R.drawable.icon_kw, ResUtil.getString(R.string.share), View.OnClickListener {
            close()
        }))
        adapter.addAll(data)
        bottomSheetView.rv_musicOp.adapter = adapter
        return@lazy dialog
    }

    private fun loadImage(url: String) {
        music.iconPath=url
        ImageLoad.load(url).into(bottomSheetView.iv_musicIcon)
    }

    fun show(music: Music) {
        this.music=music
        if (music.iconPath.isNotEmpty()) {
            loadImage(music.iconPath)
        } else {
            vm.requestPic(music.musicId)
        }
        bottomSheetView.tv_musicName.text = music.musicName
        bottomSheetView.tv_artist.text = music.artistName
        if(bottomSheet.isShowing)return
        bottomSheet.show()
    }

    fun close() {
        bottomSheet.dismiss()
    }

    data class MusicOp(val drawable: Int, val op: String, val click: View.OnClickListener?)

}