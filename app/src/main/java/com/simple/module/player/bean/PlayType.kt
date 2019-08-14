package com.simple.module.player.bean

import androidx.annotation.DrawableRes
import com.simple.R
import com.simple.tools.ResUtil

enum class PlayType(val type: String, @DrawableRes val drawable: Int) {
    ONE_CYCLE(ResUtil.getString(R.string.playType_ONE_CYCLE), R.drawable.music_type_one_loop),
    ALL_CYCLE(ResUtil.getString(R.string.playType_ALL_CYCLE), R.drawable.music_type_all_loop),
    RANDOM(ResUtil.getString(R.string.playType_RANDOM), R.drawable.icon_random),
}