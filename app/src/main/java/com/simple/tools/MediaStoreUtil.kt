package com.simple.tools

import android.content.ContentValues
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import com.simple.base.MyApplication
import java.io.File

object MediaStoreUtil {

    /**
     * 必须要传递真实地址
     */
    @JvmStatic
    fun createMediaFile(title: String, album: String, artist: String, fileName: String, dirPath: String): Uri? {
        val cv = ContentValues()
        cv.put(MediaStore.Audio.Media.DISPLAY_NAME, fileName)
        cv.put(MediaStore.Audio.Media.ALBUM, album)
        cv.put(MediaStore.Audio.Media.ARTIST, artist)
        cv.put(MediaStore.Audio.Media.TITLE, title)
        cv.put(MediaStore.Audio.Media.MIME_TYPE, "audio/" + getSuffix(fileName))
        cv.put(MediaStore.Audio.Media.DATA, dirPath + File.separator + fileName)
        cv.put(MediaStore.Audio.Media.IS_MUSIC, 1)
        return MyApplication.ctx.contentResolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, cv)
    }
    /*@JvmStatic
    fun insertVideoIntoMediaStore(context: Context, fileName: String): Uri ?{
        val contentValues = ContentValues()
        contentValues.put(MediaStore.Audio.Media.DISPLAY_NAME, fileName)
        //contentValues.put(MediaStore.Audio.Media.DATE_TAKEN, System.currentTimeMillis())
        contentValues.put(MediaStore.Audio.Media.MIME_TYPE, "video/mp4")

        return context.getContentResolver().insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, contentValues)
    }*/

    @JvmStatic
    fun createImageUri(imageName: String, dirPath: String): Uri? {
        val cv = ContentValues()
        cv.put(MediaStore.Images.ImageColumns.DISPLAY_NAME, imageName)
        cv.put(MediaStore.Images.ImageColumns.TITLE, imageName)
        cv.put(MediaStore.Images.ImageColumns.MIME_TYPE, "image/" + getSuffix(imageName))
        cv.put(MediaStore.Images.ImageColumns.DATA, dirPath + File.separator + imageName)
        return MyApplication.ctx.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv)
    }

    @JvmStatic
    fun createFileUri(fileName: String): Uri? {
        val cv = ContentValues()
        cv.put(MediaStore.Files.FileColumns.DISPLAY_NAME, fileName)
        cv.put(MediaStore.Files.FileColumns.MIME_TYPE, "text/plain")
        cv.put(MediaStore.Files.FileColumns.DATA, Environment.getExternalStorageDirectory().absolutePath + fileName)
        return MyApplication.ctx.contentResolver.insert(MediaStore.Files.getContentUri("external"), cv)
    }

    @JvmStatic
    fun getSuffix(fileName: String): String? {
        return fileName.substring(fileName.lastIndexOf('.'))
    }

    @JvmStatic
    fun queryExist(fileAbsolutePath: String): Boolean {
        val columns = arrayOf(MediaStore.Files.FileColumns.DATA)
        var exist = false
        MyApplication.ctx.contentResolver.query(
            MediaStore.Files.getContentUri("external"), columns, MediaStore.Files.FileColumns.DATA + "=?",
            arrayOf(fileAbsolutePath), null, null
        ).use {
            exist = it?.moveToFirst() ?: false
        }
        return exist
    }


}