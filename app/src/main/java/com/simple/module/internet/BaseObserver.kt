package com.simple.module.internet

import android.util.Log
import com.simple.module.internet.error.Error
import com.simple.module.internet.error.ErrorCode
import retrofit2.Call
import retrofit2.Response

/**
 * 执行请求，监听请求结果
 */
fun <T> Call<T>.observer(success: (T) -> Unit, error: ((error: Error) -> Unit)? = null) {
    val res: Response<T>
    try {
        res = this.execute()
    } catch (e: Exception) {
        e.printStackTrace()
        error?.invoke(Error(ErrorCode.CODE_UNKNOWN, e.message, e))
        return
    }
    if (res.isSuccessful) {
        success(res.body()!!)
    } else {
        error?.invoke(Error(ErrorCode.CODE_FAILED, "", null))
    }
}

/**
 * 监听转换后的数据
 */
fun <T> Transform<T>.observer(ok: (T) -> Unit, err: ((e: Error?) -> Unit)? = null) {
    if (success && data != null) {
        ok(data)
    } else {
        err?.invoke(this.error)
    }

}

/**
 * 转换数据
 */
fun <T, R> Call<T>.setTransform(transform: (T) -> R): Transform<R> {

    var trans: Transform<R>? = null
    var ok = true
    var error: Error? = null
    this.observer(
            success = {
                var res: R? = null
                try {
                    res = transform(it)
                } catch (e: Exception) {
                    e.printStackTrace()
                    error = Error(ErrorCode.CODE_TRANSFORM_ERROR, e.message, e)
                    ok = false
                }
                trans = Transform(ok, if (ok) null else error, res)
            },
            error = {
                trans = Transform(false, it, null)
            })
    return trans!!
}

fun Any?.log() {
    Log.i("log", this.toString())
}
