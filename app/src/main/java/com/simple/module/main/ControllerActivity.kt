package com.simple.module.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.simple.R
import com.simple.base.BaseActivity
import com.simple.base.BaseNavFragment
import com.simple.module.main.vm.ControllerViewModel
import com.simple.module.player.bean.PlayType
import com.simple.tools.MediaStoreUtil
import com.simple.tools.permission.PermissionUtil
import kotlinx.android.synthetic.main.activity_controller.*

class ControllerActivity : BaseActivity() {
    override fun layoutId() = R.layout.activity_controller
    var playType: PlayType = PlayType.ALL_CYCLE
    private lateinit var vm: ControllerViewModel

    override fun initView(savedInstanceState: Bundle?) {
        vm = ViewModelProviders.of(this)[ControllerViewModel::class.java]
        vm.op.observe(this, Observer {
            rootView.visibility= View.VISIBLE
            it?.addObserver(this@ControllerActivity, getObserver(it))
        })
        PermissionUtil.requestIOPermission(this, REQUEST_CODE_PERMISSION) { allGranted ->
            if (allGranted) vm.connect(this) else finish()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        BaseNavFragment.fragment?.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }


    override fun onSupportNavigateUp() = findNavController(R.id.fragment_container).navigateUp()

    companion object
}