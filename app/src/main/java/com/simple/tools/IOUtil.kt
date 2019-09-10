package com.simple.tools

import android.content.Context
import android.text.TextUtils
import androidx.annotation.WorkerThread
import com.simple.base.MyApplication
import kotlinx.coroutines.*
import java.io.*
import java.net.URL
import java.util.concurrent.atomic.AtomicBoolean

object IOUtil {


    /**
     * 复制，将一个流里面的数据复制到另一个流
     * 每隔notifyTimeGap发出一次更新通知
     */
    @JvmStatic
    @WorkerThread
    fun streamCopy(
        inputStream: InputStream?,
        outputStream: OutputStream?,
        max: Int? = -1,
        progressCallBack: ((progress: Int, length: Int?) -> Unit)? = null,
        notifyTimeGap: Long = -1,
        stopCallback: ((complete: Boolean) -> Unit)? = null,
        stop: AtomicBoolean = AtomicBoolean(false)
    ): Int {
        inputStream ?: return 0
        outputStream ?: return 0
        var offset = 0
        var update = false
        var job: Job? = null
        if (notifyTimeGap > 0) {
            job = GlobalScope.launch(Dispatchers.IO) {
                while (this.isActive) {
                    delay(notifyTimeGap)
                    update = true
                }
            }
        }
        inputStream.use { input ->
            BufferedInputStream(input).use { bis ->
                outputStream.use { out ->
                    BufferedOutputStream(out).use { bos ->
                        val byte = ByteArray(1024 * 50)
                        var length: Int
                        while (true) {
                            if (stop.get()) {
                                job?.cancel()
                                stopCallback?.invoke(false)
                                break
                            }
                            length = bis.read(byte)
                            if (length < 0) {
                                job?.cancel()
                                stopCallback?.invoke(true)
                                break
                            }
                            offset += length
                            bos.write(byte, 0, length)
                            bos.flush()
                            if (update) {
                                progressCallBack?.invoke(offset, max)
                                update = false
                            }
                        }
                    }
                }
            }

        }
        return offset
    }


    @JvmStatic
    @WorkerThread
    fun streamAccess(
        inputStream: InputStream,
        randomAccess: RandomAccessFile,
        seekTo: Long = 0, @WorkerThread progressCallBack: ((Int) -> Unit)? = null,
        notifyTimeGap: Long = -1, @WorkerThread stopCallback: ((isComplete: Boolean, length: Long) -> Unit)? = null,
        stop: AtomicBoolean = AtomicBoolean(false)
    ) {
        var update = false
        var job: Job? = null
        if (notifyTimeGap > 0) {
            job = GlobalScope.launch {
                while (isActive) {
                    delay(notifyTimeGap)
                    update = true
                }
            }
        }

        inputStream.use { input ->
            BufferedInputStream(input).use { bis ->
                randomAccess.use { ra ->
                    ra.setLength(seekTo)
                    ra.seek(seekTo)
                    var offset = 0
                    val byte = ByteArray(1024 * 50)
                    var length: Int
                    loop@ while (true) {
                        if (stop.get()) {//**判断是否暂停
                            job?.cancel()
                            stopCallback?.invoke(false, seekTo + offset)
                            break@loop
                        }
                        length = bis.read(byte)
                        if (length < 0) {//**传输完成
                            job?.cancel()
                            stopCallback?.invoke(true, seekTo + offset)
                            break
                        }
                        offset += length
                        Thread.sleep(50)
                        ra.write(byte, 0, length)
                        if (update) {
                            update = false
                            progressCallBack?.invoke(offset)//**传输过程
                        }

                    }
                }
            }
        }
    }

    @JvmStatic
    @WorkerThread
    fun onlineDataToLocal(url: String?, savePath: String) {
        onlineDataToLocal(url, savePath, null, -1, null)
    }

    fun onlineDataToLocal(
        url: String?,
        savePath: String,
        progressCallBack: ((progress: Int, length: Int?) -> Unit)? = null,
        notifyTimeGap: Long = -1,
        stopCallback: ((Boolean) -> Unit)? = null,
        stop: AtomicBoolean = AtomicBoolean(false)
    ) {
        try {
            if (TextUtils.isEmpty(url)) return
            val mUrl = URL(url)
            val outputStream = FileOutputStream(savePath)
            val con = mUrl.openConnection()
            val length = con.getHeaderField("Content-Length")
            var max = -1
            try {
                max = length.toInt()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }

            streamCopy(
                mUrl.openStream(),
                outputStream,
                max,
                progressCallBack,
                notifyTimeGap,
                stopCallback = stopCallback,
                stop = stop
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @JvmStatic
    @WorkerThread
    fun readText(inputStream: InputStream?, callback: (String?) -> Unit) {
        if (inputStream == null) {
            callback(null)
            return
        }
        inputStream.use { input ->
            BufferedInputStream(input).use { buffer ->
                callback(String(buffer.readBytes()))
            }
        }
    }


    @JvmStatic
    @WorkerThread
    fun saveObject(obj: Any, name: String) {
        MyApplication.ctx.openFileOutput(name, Context.MODE_PRIVATE).use {
            ObjectOutputStream(it).use { oos ->
                oos.writeObject(obj)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    @WorkerThread
    fun <T> readObject(name: String): T? {
        var obj: T? = null
        try {
            MyApplication.ctx.openFileInput(name).use {
                ObjectInputStream(it).use { oos ->
                    obj = oos.readObject() as? T
                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

        return obj
    }

}