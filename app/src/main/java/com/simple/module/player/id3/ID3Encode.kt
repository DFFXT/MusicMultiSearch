package com.simple.module.player.id3

import android.graphics.Bitmap
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.nio.ByteBuffer
import java.util.*

/**
 * 歌曲文件附加图片、歌词信息
 * https://www.xuebuyuan.com/93735.html
 */
class ID3Encode {
    private val infoFrame = LinkedList<ByteArray>()

    fun encode(out: OutputStream?) {
        out ?: return
        val frameLength = infoFrame.sumBy { it.size }
        if (frameLength == 0) return
        out.write(writeHead(10 + frameLength))
        for (b in infoFrame) {
            out.write(b)
        }
    }

    private fun writeHead(length: Int): ByteArray {
        val buffer = ByteBuffer.allocate(10)
        buffer.put(FrameID.ID3.name.toByteArray())
        buffer.put(3)
        buffer.put(0)
        buffer.put(0)
        val x1 = length / 128 / 128 / 128
        val x2 = length / 128 / 128 % 128
        val x3 = length / 128 % 128
        val x4 = length % 128
        buffer.put(x1.toByte())
        buffer.put(x2.toByte())
        buffer.put(x3.toByte())
        buffer.put(x4.toByte())
        return buffer.array()
    }

    fun writeBitmap(bitmap: Bitmap?): ID3Encode {
        bitmap ?: return this
        val all = bitmap.byteCount
        val out = ByteBuffer.allocate(10 + all)
        writeFrameHead(FrameID.APIC, out, all)
        val bo = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bo)
        out.put(bo.toByteArray())
        bo.close()
        infoFrame.add(out.array())
        return this
    }

    fun writeString(frameId: FrameID, data: String): ID3Encode {
        val byteArray = data.toByteArray()
        val all = byteArray.size
        val out = ByteBuffer.allocate(10 + all)
        writeFrameHead(frameId, out, all)
        out.put(byteArray)
        infoFrame.add(out.array())
        return this
    }

    private fun writeFrameHead(frameId: FrameID, buffer: ByteBuffer, frameDataLength: Int) {
        buffer.put(frameId.name.toByteArray())
        val x1 = frameDataLength / 256 / 256 / 256
        val x2 = frameDataLength / 256 / 256 % 256
        val x3 = frameDataLength / 256 % 256
        val x4 = frameDataLength % 256
        buffer.put(x1.toByte())
        buffer.put(x2.toByte())
        buffer.put(x3.toByte())
        buffer.put(x4.toByte())
        buffer.put(0)
        buffer.put(0)
    }

}

enum class FrameID {
    APIC,//图片
    ID3,
    TEXT,//歌词
    TALB,//专辑
    TOPE//艺术家
}