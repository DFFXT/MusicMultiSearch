package com.simple.tools

import com.tencent.mmkv.MMKV

object ConfigIO {
    private val kv = MMKV.defaultMMKV()
    @JvmStatic
    fun put(key: String, value: Any) {
        when (value) {
            is Int -> kv.putInt(key, value)
            is Float -> kv.putFloat(key, value)
            is String -> kv.putString(key, value)
            is Long -> kv.putLong(key, value)
            is Boolean -> kv.putBoolean(key, value)
            else -> throw Exception("value type error")
        }
    }

    fun getInt(key: String, def: Int = 0): Int {
        return kv.getInt(key, def)
    }

    fun getFloat(key: String, def: Float = 0f): Float {
        return kv.getFloat(key, def)
    }

    fun getString(key: String, def: String = ""): String {
        return kv.getString(key, def) ?: def
    }

    fun getBoolean(key: String, def: Boolean = false): Boolean {
        return kv.getBoolean(key, def)
    }

    fun getLong(key: String, def: Long): Long {
        return kv.getLong(key, def)
    }
}