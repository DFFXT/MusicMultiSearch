package com.simple.module.main.subFragmnet

import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.simple.R
import com.simple.base.BaseFragment
import com.simple.base.BaseSingleAdapter
import com.simple.bean.Music
import com.simple.module.main.vm.ControllerViewModel
import com.simple.module.player.playerInterface.PlayerObserver
import com.simple.module.player.playerInterface.PlayerOperation
import com.simple.module.search.searchResult.MusicOpBottomSheet
import com.simple.tools.MediaStoreUtil
import com.simple.tools.ResUtil
import kotlinx.android.synthetic.main.fragment_local_music.view.*

class LocalMusicFragment : BaseFragment() {
    override fun layoutId() = R.layout.fragment_local_music
    private var playerOperation: PlayerOperation? = null
    private lateinit var data:List<Music>
    private val adapter = BaseSingleAdapter<Music>(R.layout.item_search_result) { holder, index, item ->
        holder.setText(R.id.tv_musicName, item.musicName)
        holder.setText(R.id.tv_artistName, item.artistName)
        holder.setText(R.id.tv_duration, ResUtil.timeFormat("mm:ss", item.duration.toLong()))
        holder.findView<View>(R.id.iv_info).setOnClickListener {
            dialog.show(item)
        }
        holder.itemView.setOnClickListener {
            playerOperation?.removeAll()
            playerOperation?.addAll(data)
            playerOperation?.play(index)
        }
    }
    private val dialog: MusicOpBottomSheet by lazy {
        val data = ArrayList<MusicOpBottomSheet.MusicOp>()
        data.add(MusicOpBottomSheet.MusicOp(R.drawable.icon_random, ResUtil.getString(R.string.addToNextPlay)) { _, item ->
            playerOperation?.addToNext(item)
            dialog.close()
        })
        data.add(MusicOpBottomSheet.MusicOp(R.drawable.icon_random, ResUtil.getString(R.string.delete)) { _, item ->
            playerOperation?.delete(item)
            dialog.close()
        })
        MusicOpBottomSheet(context!!, data, false)
    }

    override fun initView() {
        rootView.rv_localMusic.layoutManager = LinearLayoutManager(context)
        rootView.rv_localMusic.adapter = adapter
        val vm = ViewModelProviders.of(context as FragmentActivity)[ControllerViewModel::class.java]
        vm.op.observe(this, Observer {
            playerOperation = it
            adapter.update(it?.getMusicList() ?: ArrayList())
        })
        MediaStoreUtil.queryAudio {
            this.data=it
            adapter.update(it)
        }
    }
}