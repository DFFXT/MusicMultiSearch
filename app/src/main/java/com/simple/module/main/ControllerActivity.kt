package com.simple.module.main

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
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
import com.simple.tools.MediaStoreUtil

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
        MediaStoreUtil.requestPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode== REQUEST_CODE_PERMISSION){
            for(res in grantResults){
                if(res==PackageManager.PERMISSION_DENIED){
                    finish()
                }
            }
        }
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