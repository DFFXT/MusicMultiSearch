package com.simple.module.search.model

import com.simple.bean.Lyrics
import com.simple.bean.Music
import com.simple.bean.SearchMusicRes
import com.simple.module.internet.*
import com.simple.tools.LyricsAnalysis

class BdModel : ISearchModel {
    private val http = RetrofitPack.retrofit.create(BdApis::class.java)
    override fun search(keyword: String, page: Int, pageSize: Int): Transform<SearchMusicRes> {
        return http.musicSearch(keyword, pageSize, page).setTransform {
            SearchMusicRes(it.result.songInfo.total, it.result.songInfo.songList!!.map { rowMusic ->
                Music(
                    musicId = rowMusic.songId,
                    musicName = rowMusic.musicName,
                    artistName = rowMusic.author,
                    lrcPath = rowMusic.lrcLink ?: "",
                    iconPath = rowMusic.picSmall ?: "",
                    musicPath = ""
                )
            } as ArrayList<Music>)
        }
    }

    override fun requestLrc(musicId: String): Transform<List<Lyrics>> {
        return http.musicInfo(musicId).setTransform {
            val res = ArrayList<Lyrics>()
            it.data.songList[0].lrcLink?.let { link ->
                http.requestLrc(link).observer({ rowLyrics ->
                    res.addAll(LyricsAnalysis(rowLyrics).lyricsList)
                })
            }
            res
        }
    }

    override fun requestPic(musicId: String): Transform<String> {
        return http.musicInfo(musicId).setTransform {
            it.data.songList.firstOrNull()?.singerIconBig?:""
        }
    }

    override fun requestPath(musicId: String): Transform<String> {
        return http.musicInfo(musicId).setTransform {
            it.data.songList.firstOrNull()?.songLink?:""
        }
    }
}