package com.simple.module.player.bean

import androidx.annotation.DrawableRes
import com.simple.R
import com.simple.tools.ResUtil

enum class PlayType(val type: String, @DrawableRes val drawable: Int) {
    ONE_CYCLE(ResUtil.getString(R.string.playType_ONE_CYCLE), 0),
    ALL_CYCLE(ResUtil.getString(R.string.playType_ALL_CYCLE), 0),
    RANDOM(ResUtil.getString(R.string.playType_RANDOM), 0),
}