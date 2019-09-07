package com.simple.tools.permission

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.simple.base.BaseActivity

object PermissionUtil {
    @JvmStatic
    fun requestIOPermission(activity: BaseActivity,resCode: Int,callback: (allGranted: Boolean) -> Unit){
        requestPermission(
            activity,
            resCode,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
            callback = callback
        )
    }
    @JvmStatic
    fun requestPermission(activity: BaseActivity, resCode:Int, permissions: Array<String>, callback:(allGranted:Boolean)->Unit) {
        val deny=permissions.filter {
             PackageManager.PERMISSION_DENIED == ActivityCompat.checkSelfPermission(activity,it)
        }
        if(deny.isEmpty()){
            callback.invoke(true)
        }else{
            activity.addPermissionCallback(PermissionCallback(resCode,callback))
            ActivityCompat.requestPermissions(activity, permissions, resCode)
        }
    }
}