package com.simple.module.search.searchResult

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.simple.R
import com.simple.base.BaseSingleAdapter
import com.simple.bean.Music
import com.simple.module.search.searchResult.vm.SearchViewModel
import com.simple.tools.ImageLoad
import com.simple.tools.ResUtil
import kotlinx.android.synthetic.main.layout_music_bottom_op.view.*

class MusicOpBottomSheet(ctx: Context) {
    private val bottomSheetView: View by lazy { LayoutInflater.from(ctx).inflate(R.layout.layout_music_bottom_op, null, false) }

    private val vm:SearchViewModel by lazy {
        val model=ViewModelProviders.of(ctx as FragmentActivity)[SearchViewModel::class.java]
        model.livePic.observe(ctx, Observer {
            loadImage(it)
        })
        return@lazy model
    }
    private val bottomSheet: BottomSheetDialog by lazy {
        val dialog = BottomSheetDialog(ctx)
        dialog.setContentView(bottomSheetView)
        bottomSheetView.rv_musicOp.layoutManager = LinearLayoutManager(ctx)
        val adapter= BaseSingleAdapter<MusicOp>(R.layout.item_music_op) { holder, _, item ->
            holder.setImage(R.id.iv_musicOpIcon, item.drawable)
            holder.setText(R.id.tv_musicOp, item.op)
        }
        val data=ArrayList<MusicOp>()
        data.add(MusicOp(R.drawable.icon_kw,ResUtil.getString(R.string.download)))
        data.add(MusicOp(R.drawable.icon_kw,ResUtil.getString(R.string.share)))
        adapter.addAll(data)
        bottomSheetView.rv_musicOp.adapter = adapter
        return@lazy dialog
    }

    private fun loadImage(url:String){
        ImageLoad.load(url).into(bottomSheetView.iv_musicIcon)
    }
    fun show(music:Music){
        if(music.iconPath.isNotEmpty()){
            loadImage(music.iconPath)
        }else{
            vm.requestPic(music.musicId)
        }
        bottomSheetView.tv_musicName.text=music.musicName
        bottomSheetView.tv_artist.text=music.artistName
        bottomSheet.show()
    }
    fun close(){
        bottomSheet.dismiss()
    }

    data class MusicOp(val drawable: Int, val op: String)

}