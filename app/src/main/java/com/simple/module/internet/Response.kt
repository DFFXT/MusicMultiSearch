package com.simple.module.internet

import com.simple.module.internet.error.Error


class Transform<T>(val success: Boolean, val error: Error?, val data: T?) {
    fun <R> setTransform(transform: ((T?) -> R)): Transform<R> {
        return Transform(success, error, transform(data))
    }
}