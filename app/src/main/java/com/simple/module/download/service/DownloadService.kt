package com.simple.module.download.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.simple.R
import com.simple.base.Constant
import com.simple.base.MyApplication
import com.simple.base.enquen
import com.simple.bean.Music
import com.simple.module.download.service.downloadInterface.DownloadAction
import com.simple.module.download.service.downloadInterface.DownloadOperation
import com.simple.module.internet.RetrofitPack
import com.simple.module.player.LinkedListImp
import com.simple.tools.IOUtil
import com.simple.tools.LyricsAnalysis
import com.simple.tools.MToast
import com.simple.tools.MediaStoreUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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
        startTask(music)
    }

    private fun startTask(music: Music) {
        GlobalScope.launch(Dispatchers.IO) {
            if (MediaStoreUtil.queryAudioExist(music.musicName, music.artistName)) {
                MToast.showToast(R.string.musicExist)
                return@launch
            }
            MediaStoreUtil.createMediaFile(
                music.musicName,
                music.albumName,
                music.artistName,
                music.duration,
                music.getFileName(),
                Constant.Storage.DOWNLOAD_PATH
            )?.let { uri ->
                RetrofitPack.request(music.musicPath).enquen({
                    IOUtil.streamCopy(it?.byteStream(), contentResolver.openOutputStream(uri))
                })
            }


            MediaStoreUtil.createImageUri(music.getBaseName(), "png", Constant.Storage.PIC_PATH)?.let { uri ->
                RetrofitPack.request(music.iconPath).enquen({
                    IOUtil.streamCopy(it?.byteStream(), contentResolver.openOutputStream(uri))
                })
            }


            if (!music.lrc.isNullOrEmpty()) {
                MediaStoreUtil.createFileUri(music.getBaseName(), "lrc", Constant.Storage.LYRICS_PATH)?.let {
                    MyApplication.ctx.contentResolver.openOutputStream(it)?.use { out ->
                        out.write(LyricsAnalysis.enCode(music.lrc).toByteArray())
                    }
                }
            }
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