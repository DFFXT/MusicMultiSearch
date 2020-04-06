package com.simple.module.player.id3

import android.graphics.Bitmap
import com.simple.base.Constant
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import java.io.RandomAccessFile
import java.nio.ByteBuffer

/**
 * 歌曲文件附加图片、歌词信息
 * https://www.xuebuyuan.com/93735.html 2.3讲解
 * http://id3.org/id3v2.4.0-structure 官网 2.4
 * 在文件的首部顺序记录10个字节的ID3V2.3/2.4的头部。数据结构如下：
 * char Header[3];    /*必须为"ID3"否则认为标签不存在*/
 * char Ver;    /*版本号ID3V2.3就记录3       现在最新的为4，如果是3的话，在电脑上不会显示图片，改为4就可以*/
 * char Revision;    /*副版本号此版本记录为0*/
 * char Flag;    /*存放标志的字节，这个版本只定义了三位*/
 * char Size[4];    /*标签大小，包括标签头的10个字节和所有的标签帧的大小*/  计算大小时要将0去掉，得到一个28位的二进制数，就是标签大小
 *
 * 帧头的定义如下：
 * char FrameID[4];  /*用四个字符标识一个帧，说明其内容*/
 * char Size[4];   /*帧内容的大小，不包括帧头，不得小于1，所以帧内容的开头为0，防止帧内容长度==0*/
 * char Flags[2];   /*存放标志，一般不用*/
 */
class ID3Encode {

    private var iD3Decode: ID3Decode? = null
    private var data: ByteArray? = null

    constructor(input: InputStream?) {
        iD3Decode = ID3Decode().decode(input)
    }

    fun encode(file: File) {
        if(iD3Decode==null)return
        if (!iD3Decode!!.support) return
        if (!file.exists()) return
        RandomAccessFile(file, "rw").use { out ->
            val frameLength = iD3Decode!!.frameList.sumBy { it.frame.size } + 10
            if (frameLength == 10) return
            val head = writeHead(frameLength)
            if (!iD3Decode!!.decodeSuccess) {//非id3
                data = ByteArray((file.length().toInt()))
                out.seek(0)
                out.readFully(data!!)
                out.seek(0)
                out.write(head)
                for (b in iD3Decode!!.frameList) {
                    out.write(b.frame)
                }
                out.write(data!!)
            } else {//id3 需要替换
                out.seek(iD3Decode!!.allFrameLength.toLong())
                data = ByteArray((file.length() - iD3Decode!!.allFrameLength).toInt())
                out.readFully(data!!)
                out.setLength((frameLength + data!!.size).toLong())
                out.seek(0)
                out.write(head)
                for (frame in iD3Decode!!.frameList) {
                    out.write(frame.frame)
                }
                out.write(data!!)
            }
        }


    }

    private fun writeHead(length: Int): ByteArray {
        val buffer = ByteBuffer.allocate(10)
        buffer.put(FrameID.ID3.name.toByteArray())
        buffer.put(4)
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

    /**
     * 不能直接写入bitmap
     */
    fun writeFile(): ID3Encode {
        val f = File(Constant.Storage.BASE_DIR + "/11.png")

        val all = f.length().toInt() + 13
        val out = ByteBuffer.allocate(10 + all)
        writeFrameHead(FrameID.APIC, out, all)
        //图片类型
        out.put(0)//填充0
        out.put("image/png".toByteArray())
        out.put(0)
        out.put(3)
        out.put(0)//030结束

        val bo = ByteArrayOutputStream()
        val inp = RandomAccessFile(f, "rw")
        val b = ByteArray(f.length().toInt())
        inp.seek(0)
        inp.readFully(b)
        inp.close()
        out.put(b)
        bo.close()
        addFrame(FrameID.APIC, out.array())
        return this
    }

    fun writeBitmap(bitmap: Bitmap?): ID3Encode {
        bitmap ?: return this
        val all = bitmap.byteCount + 13
        val out = ByteBuffer.allocate(10 + all)
        writeFrameHead(FrameID.APIC, out, all)
        //图片类型
        out.put(0)//填充0
        out.put("image/png".toByteArray())
        out.put(0)
        out.put(3)
        out.put(0)//030结束

        val bo = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bo)
        out.put(bo.toByteArray())
        bo.close()
        addFrame(FrameID.APIC, out.array())
        return this
    }

    fun writeString(frameId: FrameID, data: String): ID3Encode {
        val byteArray = data.toByteArray()
        val all = byteArray.size + 1
        val out = ByteBuffer.allocate(10 + all)
        writeFrameHead(frameId, out, all)
        out.put(0)
        out.put(byteArray)
        addFrame(frameId, out.array())
        return this
    }

    private fun writeFrameHead(frameId: FrameID, buffer: ByteBuffer, frameDataLength: Int) {
        buffer.put(frameId.name.toByteArray())
        val x1 = frameDataLength / 128 / 128 / 128
        val x2 = frameDataLength / 128 / 128 % 128
        val x3 = frameDataLength / 128 % 128
        val x4 = frameDataLength % 128
        buffer.put(x1.toByte())
        buffer.put(x2.toByte())
        buffer.put(x3.toByte())
        buffer.put(x4.toByte())
        buffer.put(0)
        buffer.put(0)
    }

    private fun addFrame(frameId: FrameID, data: ByteArray) {
        for (frame in iD3Decode!!.frameList) {
            if (frame.tag == frameId.name) {
                frame.frame = data
                return
            }
        }
        val f = FrameInfo()
        f.tag = frameId.name
        f.frame = data
        iD3Decode!!.frameList.add(f)
    }

    companion object {
        val MP3_HEAD = byteArrayOf(-1, 0xd8.toByte(), -1, 0xe0.toByte())//只有MP3格式支持ID3
    }
}

enum class FrameID {
    APIC,//图片
    ID3,
    TEXT,//歌词
    TALB,//专辑
    TOPE,//艺术家
    TIT2,//名称
    TPE1,//Lead performer(s)/Soloist(s) 主要演奏者
    TPE2,//Band/orchestra/accompaniment 伴奏
}