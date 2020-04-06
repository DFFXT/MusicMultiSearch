package com.simple.module.main

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.simple.R
import com.simple.base.BaseActivity
import com.simple.base.BaseNavFragment
import com.simple.module.main.vm.ControllerViewModel
import com.simple.module.player.bean.PlayType
import com.simple.tools.permission.PermissionUtil
import kotlinx.android.synthetic.main.activity_controller.*

class ControllerActivity : BaseActivity(R.layout.activity_controller) {
    var playType: PlayType = PlayType.ALL_CYCLE
    private lateinit var vm: ControllerViewModel

    override fun initView(savedInstanceState: Bundle?) {
        vm = ViewModelProvider(this)[ControllerViewModel::class.java]
        vm.op.observe(this, Observer {
            rootView.visibility = View.VISIBLE
            it?.addObserver(this@ControllerActivity, getObserver(it))
        })
        //bug 如果已经获取了权限就不会走回调
        /*prepareCall(ActivityResultContracts.RequestPermissions()){
            Log.i("logInfo","xxxxxxxxxx")
            if (it.containsValue(false)) finish() else vm.connect(this)
        }.launch(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE))*/
        PermissionUtil.requestIOPermission(this, REQUEST_CODE_PERMISSION) { allGranted ->
            if (allGranted) vm.connect(this) else finish()
        }
        if (Build.VERSION.SDK_INT >= 29) {
            startActivityForResult(Intent(Intent.ACTION_OPEN_DOCUMENT_TREE), 10)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        BaseNavFragment.fragment?.onActivityResult(requestCode, resultCode, data)
        if(requestCode ==10){
            Log.i("logInfo","$data")
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    override fun onSupportNavigateUp() = findNavController(R.id.fragment_container).navigateUp()

    companion object
}