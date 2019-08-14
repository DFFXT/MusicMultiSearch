package com.simple.module.main

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.navigation.findNavController
import com.simple.R
import com.simple.base.BaseActivity
import com.simple.base.BaseNavFragment
import com.simple.module.player.MusicPlayer
import com.simple.module.player.bean.PlayType
import com.simple.module.player.playerInterface.PlayerObserver
import com.simple.module.player.playerInterface.PlayerOperation

class ControllerActivity : BaseActivity() {
    override fun layoutId() = R.layout.activity_controller
    var playType: PlayType = PlayType.ALL_CYCLE
    private val observer: PlayerObserver by lazy {
        getObserver()
    }
    private val connection = object : ServiceConnection {
        override fun onServiceDisconnected(p0: ComponentName?) {
            op = null
        }

        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            op = p1 as? PlayerOperation
            op?.addObserver(this@ControllerActivity, observer)
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        bindService(Intent(this, MusicPlayer::class.java), connection, Context.BIND_AUTO_CREATE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        BaseNavFragment.fragment?.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(connection)
        op = null
    }

    override fun onSupportNavigateUp() = findNavController(R.id.fragment_container).navigateUp()

    companion object {
        @JvmStatic
        var op: PlayerOperation? = null
    }
}