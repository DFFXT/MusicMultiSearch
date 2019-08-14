package com.simple.module.download.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.simple.R
import com.simple.base.Constant
import com.simple.bean.Music
import com.simple.module.download.service.downloadInterface.DownloadAction
import com.simple.module.download.service.downloadInterface.DownloadOperation
import com.simple.module.player.LinkedListImp
import com.simple.tools.IOUtil
import com.simple.tools.MToast
import com.simple.tools.MediaStoreUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.URL
import java.util.*

class DownloadService : Service() {
    private val op = DownloadOperationImp()
    private val taskList = LinkedListImp<Music>(LinkedList())
    override fun onBind(p0: Intent?): IBinder? {
        return op
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        when (DownloadAction.valueOf(intent.action!!)) {
            DownloadAction.ADD_TASK -> {
                val data = intent.getSerializableExtra(DATA) as Music
                addTaskAndStart(data)
            }
        }
        return START_NOT_STICKY
    }


    private inner class DownloadOperationImp : Binder(), DownloadOperation {
        override fun start() {

        }

        override fun pause() {

        }

        override fun cancel() {

        }
    }

    private fun addTaskAndStart(music: Music) {
        if (taskList.containMusic(music)) return
        taskList.add(music)
    }
    private fun startTask(music: Music){
        GlobalScope.launch(Dispatchers.IO) {
            if(MediaStoreUtil.queryExist(music.getAbsolutePath())){
                MToast.showToast(R.string.musicExist)
                return@launch
            }
            val uri = MediaStoreUtil.createMediaFile(
                music.musicName,
                music.albumName,
                music.artistName,
                music.getFileName(),
                Constant.Storage.DOWNLOAD_PATH
            ) ?: return@launch
            val out = contentResolver.openOutputStream(uri)!!
            val con = URL(music.musicPath).openConnection().getInputStream()
            IOUtil.streamCopy(con, out)
        }
    }

    companion object {
        const val DATA = "data"
        @JvmStatic
        fun addTask(ctx: Context, music: Music) {
            val intent = Intent(ctx, DownloadService::class.java)
            intent.action = DownloadAction.ADD_TASK.name
            intent.putExtra(DATA, music)
            ctx.startService(intent)
        }
    }
}