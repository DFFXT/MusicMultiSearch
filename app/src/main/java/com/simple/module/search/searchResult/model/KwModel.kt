package com.simple.module.search.searchResult.model


import com.simple.bean.Lyrics
import com.simple.bean.Music
import com.simple.bean.SearchMusicRes
import com.simple.module.internet.KwApis
import com.simple.module.internet.RetrofitPack
import com.simple.module.internet.Transform
import com.simple.module.internet.setTransform
import com.simple.tools.ResUtil

class KwModel : ISearchModel {
    private val http = RetrofitPack.retrofit.create(KwApis::class.java)
    override fun search(keyword: String, page: Int, pageSize: Int): Transform<SearchMusicRes> {
        return http.search(keyword, page, pageSize).setTransform {
            return@setTransform SearchMusicRes(
                    total = it.data.total,
                    data = it.data.list.map { rowMusic ->
                        Music(
                                musicId = rowMusic.musicrid,
                                musicName = rowMusic.name,
                                albumName = rowMusic.album,
                                duration = ResUtil.timeFormat("mm:ss", rowMusic.duration * 1000L),
                                artistName = rowMusic.artist,
                                iconPath = rowMusic.pic,
                                musicPath = "",
                                lrcPath = ""
                        )
                    } as ArrayList<Music>
            )
        }
    }

    override fun requestLrc(musicId: String): Transform<List<Lyrics>> {
        return http.requestInfo(musicId.drop("MUSIC_".length)).setTransform {
            if (it.data == null) {
                return@setTransform arrayListOf<Lyrics>()
            }
            return@setTransform it.data.lrclist.map { rowLrc ->
                Lyrics(millsTime = (rowLrc.time.toFloat() * 1000).toInt(), lyrics = rowLrc.lineLyric)
            }
        }
    }

    override fun requestPic(musicId: String): Transform<String> {
        return http.requestInfo(subId(musicId)).setTransform {
            return@setTransform it.data.songinfo.pic
        }
    }

    override fun requestPath(musicId: String): Transform<String> {
        return http.requestPath(subId(musicId)).setTransform {
            return@setTransform it.url
        }
    }

    private fun subId(musicId: String): String {
        return musicId.drop("MUSIC_".length)
    }

}