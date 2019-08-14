package com.simple.module.search.searchResult.model


import com.simple.bean.Lyrics
import com.simple.bean.Music
import com.simple.bean.SearchMusicRes
import com.simple.module.internet.KgApis
import com.simple.module.internet.RetrofitPack
import com.simple.module.internet.Transform
import com.simple.module.internet.setTransform
import com.simple.module.search.searchResult.vm.Source
import com.simple.tools.LyricsAnalysis
import com.simple.tools.ResUtil

class KgModel : ISearchModel {
    private val http = RetrofitPack.retrofit.create(KgApis::class.java)
    override fun search(keyword: String, page: Int, pageSize: Int): Transform<SearchMusicRes> {
        return http.search(keyword, page, pageSize).setTransform {
            return@setTransform SearchMusicRes(
                total = it.data.total,
                data = it.data.lists.map { rowMusic ->
                    Music(
                        musicId = rowMusic.fileHash,
                        musicName = rowMusic.songName.replace("<em>", "").replace("</em>", ""),
                        albumName = rowMusic.albumName,
                        duration = rowMusic.duration*1000,
                        artistName = rowMusic.singerName.replace("<em>", "").replace("</em>", ""),
                        iconPath = "",
                        musicPath = "",
                        lrcPath = "",
                        source = Source.KG
                    )
                } as ArrayList<Music>
            )
        }
    }

    override fun requestLrc(musicId: String): Transform<List<Lyrics>> {
        return http.requestInfo(musicId).setTransform {
            return@setTransform LyricsAnalysis(it.data.lyrics).lyricsList
        }
    }

    override fun requestPic(musicId: String): Transform<String> {
        return http.requestInfo(musicId).setTransform {
            return@setTransform it.data.img
        }
    }

    override fun requestPath(musicId: String): Transform<String> {
        return http.requestInfo(musicId).setTransform {
            return@setTransform it.data.play_url
        }
    }
}