package com.simple.module.download.service.downloadInterface

interface DownloadObserver {
    fun onComplete(){}
    fun onStart(){}
    fun onListChange(){}
}
interface DownloadOperation{
    fun start()
    fun pause()
    fun cancel()
}
enum class DownloadAction{
    ADD_TASK
}