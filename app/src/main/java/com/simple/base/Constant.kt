package com.simple.base

import android.os.Environment
import java.io.File

class Constant {
    object Storage {
        @JvmStatic
        val BASE_DIR: String = Environment.getExternalStorageDirectory().absolutePath
        @JvmStatic
        val DOWNLOAD_PATH = BASE_DIR +File.separator+"Music"
        @JvmStatic
        //val PIC_PATH="$BASE_DIR/cache/singer"
        val PIC_PATH = BASE_DIR + File.separator + "Pictures"
        @JvmStatic
        val LYRICS_PATH = DOWNLOAD_PATH
    }
}