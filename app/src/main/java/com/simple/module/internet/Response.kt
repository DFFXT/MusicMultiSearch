package com.simple.module.internet


class Transform<T>(val success: Boolean, val code: Int, val data: T?) {
    fun <R> setTransform(transform: ((T?) -> R)): Transform<R> {
        return Transform(success, code, transform(data))
    }
}