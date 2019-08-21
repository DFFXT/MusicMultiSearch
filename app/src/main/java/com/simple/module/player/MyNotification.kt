package com.simple.module.player

import android.content.Context
import android.widget.RemoteViews
import com.simple.R
import com.simple.base.BaseCustomNotification
import com.simple.bean.Music

class MyNotification(ctx:Context):BaseCustomNotification(ctx,1,"1","1", R.layout.layout_loading) {
    private lateinit var music: Music
    private var isPlaying: Boolean=false
    fun notifyLoadChange(music: Music) {
        this.music=music
        notifyChange()
    }
    fun notifyStatusChange(isPlaying:Boolean){
        this.isPlaying=isPlaying
        clear=!isPlaying
        notifyChange()
    }
    override fun update(view: RemoteViews) {

    }
}