package com.simple.base

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.CallSuper
import com.simple.R

/**
 * 原生通知
 */
abstract class BaseNotification(private val context: Context,
                                private val notificationId:Int,
                                id: String,
                                name: String) {
    var builder: Notification.Builder
    var clear=false

    init {
        createChannel(id, name)
        builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(context, id)
        } else {
            Notification.Builder(context)
        }
        builder.setSmallIcon(R.mipmap.ic_launcher)
    }

    private fun createChannel(id: String, name: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_DEFAULT)
            channel.setSound(null,null)

            manager().createNotificationChannel(channel)
        }
    }
    protected fun manager():NotificationManager{
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }


    @CallSuper
    open fun notifyChange() {
        builder.setAutoCancel(false)
        update(builder)
        val notification = builder.build()
        if(!clear){
            notification.flags = Notification.FLAG_NO_CLEAR
        }else{
            notification.flags = Notification.FLAG_LOCAL_ONLY
        }


        manager().notify(notificationId,notification)
    }

    fun cancel() {
        manager().cancel(notificationId)
    }

    abstract fun update(builder: Notification.Builder)


}