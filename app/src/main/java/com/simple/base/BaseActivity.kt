package com.simple.base

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.simple.module.internet.log
import com.simple.tools.WindowUtil
import com.simple.tools.permission.PermissionCallback

abstract class BaseActivity : AppCompatActivity() {
    private var permissionCallback: HashMap<Int, PermissionCallback>? = null

    var topInset = 0
    var bottomInset = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowUtil.setTransparentStatusBar(window)
        WindowUtil.setLightStatus(window)
        if (layoutId() != 0) {
            setContentView(layoutId())
        }
        beforeView()
        initView(savedInstanceState)
        window.decorView.setOnApplyWindowInsetsListener { view, windowInsets ->
            topInset = windowInsets.systemWindowInsetTop
            bottomInset = windowInsets.systemWindowInsetBottom
            onInset()
            return@setOnApplyWindowInsetsListener view.onApplyWindowInsets(windowInsets)
        }
    }


    abstract fun layoutId(): Int
    abstract fun initView(savedInstanceState: Bundle?)

    open fun beforeView() {

    }

    open fun onInset() {

    }

    fun addPermissionCallback(callback: PermissionCallback) {
        permissionCallback = permissionCallback ?: HashMap(4)
        permissionCallback?.put(callback.resCode, callback)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(permissions.isEmpty())return
        requestCode.log("sdffff")
        permissionCallback?.get(requestCode)?.callback?.invoke(!grantResults.contains(PackageManager.PERMISSION_DENIED))
        permissionCallback?.remove(requestCode)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    companion object {
        const val REQUEST_CODE_PERMISSION = 123
    }
}