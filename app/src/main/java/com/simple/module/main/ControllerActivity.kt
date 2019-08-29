package com.simple.module.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.simple.R
import com.simple.base.BaseActivity
import com.simple.base.BaseNavFragment
import com.simple.module.main.vm.ControllerViewModel
import com.simple.module.player.bean.PlayType
import com.simple.module.player.id3.ID3Encode
import com.simple.tools.MediaStoreUtil

class ControllerActivity : BaseActivity() {
    override fun layoutId() = R.layout.activity_controller
    var playType: PlayType = PlayType.ALL_CYCLE
    private lateinit var vm: ControllerViewModel

    override fun initView(savedInstanceState: Bundle?) {
        vm = ViewModelProviders.of(this)[ControllerViewModel::class.java]
        vm.op.observe(this, Observer {
            it?.addObserver(this@ControllerActivity, getObserver(it))
        })
        vm.connect(this)
        MediaStoreUtil.requestPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSION) {
            for (res in grantResults) {
                if (res == PackageManager.PERMISSION_DENIED) {
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
    }

    override fun onSupportNavigateUp() = findNavController(R.id.fragment_container).navigateUp()

    companion object
}