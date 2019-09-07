package com.simple.module.cNative

class CNative {
    external fun main(a:String):ByteArray
    init {
        System.loadLibrary("native-lib")
    }
}