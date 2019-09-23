package com.simple.base

import android.net.Uri
import android.os.Environment
import com.simple.tools.IOUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

class Constant {
    object Storage {
        @JvmStatic
        var BASE_URI: Uri? = null
        @JvmStatic
        val BASE_DIR: String = Environment.getExternalStorageDirectory().absolutePath
        @JvmStatic
        val DOWNLOAD_PATH = BASE_DIR + File.separator + Environment.DIRECTORY_MUSIC
        @JvmStatic
        //val PIC_PATH="$BASE_DIR/cache/singer"
        val PIC_PATH: String =
            MyApplication.ctx.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!.absolutePath
        @JvmStatic
        val LYRICS_PATH: String =
            MyApplication.ctx.getExternalFilesDir(Environment.DIRECTORY_MUSIC)!!.absolutePath

        init {
            //BASE_URI = IOUtil.readObject<Uri>("uri")
        }

        @JvmStatic
        fun saveUri() {
            GlobalScope.launch(Dispatchers.IO) {
                IOUtil.saveObject(BASE_URI, "uri")
            }
        }
    }
}