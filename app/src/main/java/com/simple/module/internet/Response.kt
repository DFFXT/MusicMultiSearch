package com.simple.module.internet

import com.simple.module.internet.error.Error


class Transform<T>(val success: Boolean, val error: Error?, val data: T?) {
    init {
        error?.printStackTrace()
    }
    fun <R> setTransform(transform: ((T?) -> R)): Transform<R> {
        return Transform(success, error, transform(data))
    }

    fun ok(block: (T) -> Unit) {
        if (success && data != null) {
            block(data)
        }
    }

    fun err(block: (Error?) -> Unit) {
        if (!success || data == null) {
            block(error)
        }
    }

    /**
     * 监听转换后的数据
     * gradle风格
     * observer{
     *      ok{...}
     *      err{...}
     * }
     */
    fun observer(block: Transform<T>.() -> Unit) {
        this.apply(block)
    }
}