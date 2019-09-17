package com.simple.tools

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import com.simple.base.MyApplication
import com.simple.base.ifNotNullOrBlank
import com.simple.bean.Music
import com.simple.module.internet.log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*

object MediaStoreUtil {

    /**
     * 必须要传递真实地址
     */
    @JvmStatic
    fun createMediaFile(
        title: String,
        album: String,
        artist: String,
        duration: Int,
        fileName: String,
        dirPath: String
    ): Uri? {
        val cv = ContentValues()
        cv.put(MediaStore.Audio.Media.DISPLAY_NAME, fileName)
        cv.put(MediaStore.Audio.Media.ALBUM, album)
        cv.put(MediaStore.Audio.Media.ARTIST, artist)
        cv.put(MediaStore.Audio.Media.TITLE, title)
        cv.put(MediaStore.Audio.Media.MIME_TYPE, "audio/" + getSuffix(fileName))
        cv.put(MediaStore.Audio.Media.DATA, dirPath + File.separator + fileName)
        cv.put(MediaStore.Audio.Media.IS_MUSIC, 1)
        cv.put(MediaStore.Audio.Media.DURATION, duration)
        return MyApplication.ctx.contentResolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, cv)
    }


    @JvmStatic
    fun createImageUri(imageName: String, suffix: String, dirPath: String): Uri? {
        val cv = ContentValues()
        cv.put(MediaStore.Images.ImageColumns.DISPLAY_NAME, "$imageName.$suffix")
        cv.put(MediaStore.Images.ImageColumns.TITLE, imageName)
        cv.put(MediaStore.Images.ImageColumns.DURATION, "image/$suffix")
        cv.put(MediaStore.Images.ImageColumns.DATA, dirPath + File.separator + imageName + "." + suffix)
        return MyApplication.ctx.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv)
    }

    @JvmStatic
    fun getImageUri(title: String): Uri? {
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val columns = arrayOf(MediaStore.Images.Media._ID)
        var id = 0
        MyApplication.ctx.contentResolver.query(
            uri,
            columns,
            MediaStore.Images.ImageColumns.TITLE + "=?",
            arrayOf(title),
            null,
            null
        )
            .use {
                it ?: return null
                if (it.moveToNext()) {
                    id = it.getInt(0)
                    return@use
                } else {
                    return null
                }
            }
        return MediaStore.Images.Media.EXTERNAL_CONTENT_URI.buildUpon().appendPath("$id").build()
    }

    @JvmStatic
    fun createFileUri(fileName: String, suffix: String?, dirPath: String): Uri? {
        val cv = ContentValues()
        val displayName = fileName + suffix.ifNotNullOrBlank(".$suffix")
        cv.put(MediaStore.Files.FileColumns.DISPLAY_NAME, displayName)
        cv.put(MediaStore.Files.FileColumns.TITLE, fileName)
        cv.put(MediaStore.Files.FileColumns.DATA, Environment.getExternalStorageDirectory().absolutePath +dirPath+File.separator+ displayName)
        return MyApplication.ctx.contentResolver.insert(MediaStore.Files.getContentUri("external"), cv)
    }

    @JvmStatic
    fun getSuffix(fileName: String): String? {
        return fileName.substring(fileName.lastIndexOf('.'))
    }

    @JvmStatic
    fun queryAudioExist(musicName: String, artist: String): Boolean {
        val columns = arrayOf(MediaStore.Audio.AudioColumns.TITLE, MediaStore.Audio.AudioColumns.ARTIST)
        var exist = false
        MyApplication.ctx.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            columns,
            MediaStore.Audio.AudioColumns.TITLE + "=? and " + MediaStore.Audio.AudioColumns.ARTIST + "=?",
            arrayOf(musicName, artist),
            null,
            null
        ).use {
            exist = it?.moveToFirst() ?: false
        }
        return exist
    }

    @JvmStatic
    fun queryExist(fileAbsolutePath: String): Int {
        val columns = arrayOf(MediaStore.Files.FileColumns._ID)
        var id = -1
        MyApplication.ctx.contentResolver.query(
            MediaStore.Files.getContentUri("external"), columns, MediaStore.Files.FileColumns.DATA + "=?",
            arrayOf(fileAbsolutePath), null, null
        ).use {
            it?:return@use
            if(it.moveToFirst()){
                id=it.getInt(0)
            }
        }
        return id
    }

    /**
     * 根据id获取uri
     */
    @JvmStatic
    fun getAudioUri(id:String):Uri{
        return MediaStore.Audio.Media.EXTERNAL_CONTENT_URI.buildUpon().appendPath(id).build()
    }

    @JvmStatic
    fun getFileUri(id:String):Uri{
        return MediaStore.Files.getContentUri("external").buildUpon().appendPath(id).build()
    }

    @JvmStatic
    fun queryAudio(callback: (List<Music>) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            val list = LinkedList<Music>()
            MyApplication.ctx.contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null)
                .use {
                    it ?: return@use
                    while (it.moveToNext()) {
                        val item=Music(
                            musicId = it.getString(it.getColumnIndex(MediaStore.Audio.AudioColumns._ID)),
                            musicPath = it.getString(it.getColumnIndex(MediaStore.Audio.AudioColumns.DATA)),
                            musicName = it.getString(it.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE)),
                            artistName = it.getString(it.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST))
                                ?: "",
                            albumName = it.getString(it.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM)) ?: "",
                            duration = it.getInt(it.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION)),
                            source = null,
                            iconPath = "",
                            lrc = null
                        )
                        item.localDisplayName = it.getString(it.getColumnIndex(MediaStore.Audio.AudioColumns.DISPLAY_NAME))
                        list.add(item)
                    }
                }
            withContext(Dispatchers.Main) {
                callback(list)
            }
        }

    }
    fun queryAudioPath(uri: Uri):String?{
        var res:String?=null
        MyApplication.ctx.contentResolver.query(uri, null, null, null, null)
            .use {
                it ?: return@use
                if (it.moveToNext()) {
                    res=it.getString(it.getColumnIndex(MediaStore.Audio.AudioColumns.DATA))
                }
            }
        return res
    }
    fun queryFiles() {
        GlobalScope.launch(Dispatchers.IO) {
            val list = LinkedList<Music>()
            MyApplication.ctx.contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null)
                .use {
                    it ?: return@launch
                    while (it.moveToNext()) {
                        var res = ""
                        var i = 0
                        while (i < it.columnCount) {
                            res += it.getColumnName(i) + ":" + it.getString(i) + "\n"
                            i++
                        }
                        res.log()

                    }
                }
        }

    }

    @JvmStatic
    fun createF(ctx:Activity){
        val intent=Intent(Intent.ACTION_CREATE_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type="image/*"
        ctx.startActivityForResult(intent,0)
    }

}