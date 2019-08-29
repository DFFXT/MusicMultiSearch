package com.simple.module.player.id3

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.simple.base.MyApplication
import com.simple.base.read
import com.simple.bean.Lyrics
import com.simple.tools.LyricsAnalysis
import java.io.InputStream
import kotlin.experimental.and

/**
 * 从歌曲文件中读取图片、歌词
 */
class ID3Decode {

    var bitmap: Bitmap? = null
        private set
    var lyrics: ArrayList<Lyrics>? = null
        private set


    fun decode(input: InputStream) {
        val head = input.read(10) ?: return
        if (String(head, 0, 3) != FrameID.ID3.name) {
            return
        }
        val size =
            ((head[6] and 0x7F) * 128 * 128 * 128 + (head[7] and 0x7F) * 128 * 128 + (head[8] and 0x7F) * 128
                    + (head[9] and 0x7F))
        val allFrame = input.read(size - 10) ?: return

        var start = 0
        while (true) {
            if (start >= allFrame.size) break
            val tag = FrameID.valueOf(String(allFrame, start, 4))
            val frameSize =
                ((allFrame[start + 4].toInt() and 0xff shl 24) + (allFrame[start + 5].toInt() and 0xff shl 16) + (allFrame[start + 6].toInt() and 0xff shl 8) + allFrame[start + 7])
            when (tag) {
                FrameID.APIC -> {
                    bitmap = BitmapFactory.decodeByteArray(allFrame, start + 10, frameSize)
                    start += 10 + frameSize
                }
                FrameID.TEXT -> {
                    lyrics = LyricsAnalysis(String(allFrame, start + 10, frameSize)).lyricsList
                    start += 10 + frameSize
                }
            }
        }
    }

    fun decode(uri: Uri): ID3Decode {
        bitmap = null
        lyrics = null
        val input = MyApplication.ctx.contentResolver.openInputStream(uri) ?: return this
        input.use {
            decode(it)
        }
        return this

    }

}