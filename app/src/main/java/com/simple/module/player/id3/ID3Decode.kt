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
import kotlin.math.min

/**
 * 从MP3歌曲文件中读取图片、歌词
 */
class ID3Decode {
    //是否支持id3
    var support = true
        private set
    var decodeSuccess = false
        private set

    var frameList: ArrayList<FrameInfo> = arrayListOf()
    var bitmapFrame: ByteArray? = null
        private set
    var bitmap: Bitmap? = null
        private set
    var lyricsFrame: ByteArray? = null
        private set
    var lyrics: ArrayList<Lyrics>? = null
        private set
    var titleFrame: ByteArray? = null
        private set
    var title: String? = null
        private set

    var allFrameLength = 0
        private set

    fun decode(input: InputStream?): ID3Decode {
        input?.use {
            val head = input.read(10) ?: return this

            if (String(head, 0, 3) != FrameID.ID3.name) {
                support =
                    (head[0] == ID3Encode.MP3_HEAD[0] && head[1] == ID3Encode.MP3_HEAD[1] && head[2] == ID3Encode.MP3_HEAD[2] && head[3] == ID3Encode.MP3_HEAD[3])
                return this
            }
            val size =
                ((head[2 + 4].toInt() and 0x7f) * 128 * 128 * 128 + (head[2 + 5].toInt() and 0xff) * 128 * 128 + (head[2 + 6].toInt() and 0xff) * 128 + head[2 + 7])
            val allFrame = input.read(size - 10) ?: return this
            allFrameLength = size
            decodeSuccess = true
            var start = 0
            while (true) {
                if (start >= allFrame.size) break
                if (allFrame[start].toInt() == 0 || allFrame[start + 1].toInt() == 0 || allFrame[start + 2].toInt() == 0 || allFrame[start + 3].toInt() == 0) break
                val tag = (String(allFrame, start, 4))
                //不包括10字节的头部
                val frameSize =
                    ((allFrame[start - 2 + 6] and 0x7F) * 128 * 128 * 128 + (allFrame[start - 2 + 7] and 0x7F) * 128 * 128 + (allFrame[start - 2 + 8] and 0x7F) * 128
                            + (allFrame[start - 2 + 9] and 0x7F))
                if (frameSize + start + 10 > size) return@use
                val frame = FrameInfo()
                frame.tag = tag
                frame.frame = allFrame.copyOfRange(start, start + frameSize + 10)
                when (tag) {
                    //图片帧以 0image/png030 或 0image/jpeg030 开始
                    FrameID.APIC.name -> {
                        var s = 0
                        for (i in start + 11 until start + 11 + 13 + 11) {//寻找030
                            if (allFrame[i].toInt() == 0 && allFrame[i + 1].toInt() == 3 && allFrame[i + 2].toInt() == 0) {
                                s = i + 3
                                break
                            }
                        }
                        var index = 0
                        while (s == 0 && index < TypeArray.size) {//没有030寻找图片开头
                            s = findPicStart(
                                allFrame,
                                start + 11,
                                min(14, frameSize),
                                TypeArray[index]
                            )
                            index++
                        }
                        bitmap = BitmapFactory.decodeByteArray(allFrame, s, frameSize - (s - start))
                    }
                    FrameID.TEXT.name -> {
                        lyrics =
                            LyricsAnalysis(String(allFrame, start + 11, frameSize - 1)).lyricsList
                    }
                    FrameID.TIT2.name -> {
                        title = String(allFrame, start + 11, frameSize - 1)
                    }
                    /*else -> {
                        return
                    }*/
                }
                start += 10 + frameSize
            }
        }
        decodeSuccess = true
        return this
    }

    fun decode(uri: Uri): ID3Decode {
        bitmap = null
        lyrics = null
        val input = MyApplication.ctx.contentResolver.openInputStream(uri) ?: return this
        decode(input)
        return this
    }

    private fun findPicStart(data: ByteArray, offset: Int, length: Int, byteArray: ByteArray): Int {
        for (i in offset until length + offset) {
            if (data[i] == byteArray[0] && data[i + 1] == byteArray[1] && data[i + 2] == byteArray[2] && data[i + 3] == byteArray[3]) {
                return i
            }
        }
        return 0
    }


    companion object {
        private val JPEG_START = byteArrayOf(-1, -40, -1, -31)
        private val XXX_START = byteArrayOf(-1, -40, -1, -32)//不知道什么格式，反正存在这种图片
        private val PNG_START = byteArrayOf(-119, 80, 78, 71)
        private val TypeArray = arrayListOf(JPEG_START, PNG_START, XXX_START)
    }
}