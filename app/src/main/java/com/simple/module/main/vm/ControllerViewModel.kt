package com.simple.module.main.vm

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.MutableLiveData
import com.simple.base.BaseViewModel
import com.simple.module.player.MusicPlayer
import com.simple.module.player.playerInterface.PlayerOperation
import java.lang.ref.WeakReference

class ControllerViewModel : BaseViewModel() {
    val op = MutableLiveData<PlayerOperation?>()
    private var ctx: WeakReference<Context>?=null
    private val connection = object : ServiceConnection {
        override fun onServiceDisconnected(p0: ComponentName?) {
            op.value = null
        }

        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            op.value = p1 as? PlayerOperation
        }
    }

    fun connect(ctx: Context) {
        this.ctx = WeakReference(ctx)
        ctx.startService(Intent(ctx, MusicPlayer::class.java))
        ctx.bindService(Intent(ctx, MusicPlayer::class.java), connection, Context.BIND_AUTO_CREATE)
    }

    override fun onCleared() {
        super.onCleared()
        ctx?.get()?.unbindService(connection)
    }
}