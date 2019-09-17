package com.simple.module.search.searchResult.model

import com.simple.bean.Lyrics
import com.simple.bean.Music
import com.simple.bean.SearchMusicRes
import com.simple.module.internet.*
import com.simple.module.search.searchResult.vm.Source
import com.simple.tools.LyricsAnalysis

class BdModel : ISearchModel {
    private val http = RetrofitPack.retrofit.create(BdApis::class.java)
    override fun search(keyword: String, page: Int, pageSize: Int): Transform<SearchMusicRes> {
        return http.musicSearch(keyword, pageSize, page).setTransform {
            SearchMusicRes(it.result.songInfo.total, it.result.songInfo.songList!!.map { rowMusic ->
                Music(
                    musicId = rowMusic.songId,
                    musicName = rowMusic.musicName + "  " + rowMusic.info,
                    albumName = rowMusic.albumTitle,
                    duration = rowMusic.duration.toInt()*1000,
                    artistName = rowMusic.author,
                    lrc = null,
                    iconPath = rowMusic.picSmall ?: "",
                    musicPath = "",
                    source = Source.BD
                )
            } as ArrayList<Music>)
        }
    }

    override fun requestLrc(musicId: String): Transform<List<Lyrics>> {
        return http.requestInfo(musicId).setTransform {
            val res = ArrayList<Lyrics>()
            it.songInfo.lrcLink.let { link ->
                http.requestLrc(link).observer({ rowLyrics ->
                    res.addAll(LyricsAnalysis(rowLyrics).lyricsList)
                })
            }
            res
        }
    }

    override fun requestPic(musicId: String): Transform<String> {
        return http.requestInfo(musicId).setTransform {
            it.songInfo.artistPic500x500
        }
    }

    override fun requestPath(musicId: String): Transform<String> {
        return http.requestInfo(musicId).setTransform {
            it.bitRate.songLink
        }
    }
}