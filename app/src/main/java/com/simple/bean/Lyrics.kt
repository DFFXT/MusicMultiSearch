package com.simple.bean

import java.io.Serializable

data class Lyrics(
    val millsTime: Int,
    val lyrics: String
) : Serializable {
    var width = 0
    var height = 0
}