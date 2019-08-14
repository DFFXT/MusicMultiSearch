package com.simple.base

import android.os.Environment
import java.io.File

class Constant {
    object Storage {
        @JvmStatic
        val BASE_DIR = Environment.getExternalStorageDirectory().absolutePath + File.separator + "/0"
        @JvmStatic
        val DOWNLOAD_PATH = "$BASE_DIR/cache/download"
    }
}