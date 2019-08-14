package com.simple.module.internet

import android.util.Log
import com.simple.module.internet.error.ErrorCode
import retrofit2.Call
import retrofit2.Response

/**
 * 执行请求，监听请求结果
 */
fun <T> Call<T>.observer(success: (T) -> Unit, error: ((code: Int) -> Unit)? = null) {
    val res: Response<T>
    try {
        res = this.execute()
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
        error?.invoke(ErrorCode.CODE_UNKNOWN)
        return
    }
    if (res.isSuccessful) {
        success(res.body()!!)
    } else {
        error?.invoke(ErrorCode.CODE_FAILED)
    }
}

/**
 * 监听转换后的数据
 */
fun <T> Transform<T>.observer(ok: (T) -> Unit, error: ((code: Int) -> Unit)? = null) {
    if (success&&data!=null) {
        ok(data)
    } else {
        error?.invoke(code)
    }

}

/**
 * 转换数据
 */
fun <T, R> Call<T>.setTransform(transform: (T) -> R): Transform<R> {

    var trans: Transform<R>? = null
    this.observer(
        success = {
            trans = Transform(true, ErrorCode.CODE_OK, transform(it))
        },
        error = {
            trans = Transform(false, it, null)
        })
    return trans!!
}

fun Any?.log() {
    Log.i("log", this.toString())
}
