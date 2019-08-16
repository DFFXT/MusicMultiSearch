package com.simple.module.search.searchResult.model

import com.simple.bean.Lyrics
import com.simple.bean.Music
import com.simple.bean.SearchMusicRes
import com.simple.module.internet.QqApis
import com.simple.module.internet.RetrofitPack
import com.simple.module.internet.Transform
import com.simple.module.internet.error.ErrorCode
import com.simple.module.internet.setTransform
import com.simple.module.search.searchResult.vm.Source
import com.simple.tools.LyricsAnalysis
import java.util.regex.Pattern

/**
 * musicId字段 需要保存 id和mid 使用分隔符 “?”
 */
class QqModel : ISearchModel {
    private val http = RetrofitPack.retrofit.create(QqApis::class.java)
    override fun search(keyword: String, page: Int, pageSize: Int): Transform<SearchMusicRes> {
        return http.search(keyword, page, pageSize).setTransform {
            SearchMusicRes(it.data.song.totalnum, it.data.song.list.map { row ->
                var artist = ""
                row.singer.forEach { singleArtist ->
                    artist += singleArtist.name
                }
                Music(
                    musicId = row.id + "?" + row.mid,
                    musicName = row.title + "  " + row.subTitle,
                    albumName = row.album.name,
                    duration = row.duration*1000,
                    artistName = artist,
                    iconPath = "",
                    lrc = null,
                    musicPath = "",
                    source = Source.QQ
                )
            } as ArrayList<Music>)

        }
    }

    private val pattern = Pattern.compile("&#([0-9]{1,2});")
    override fun requestLrc(musicId: String): Transform<List<Lyrics>> {
        val id = musicId.substring(0, musicId.indexOf('?'))
        return http.requestLrc(id).setTransform {
            val matcher = pattern.matcher(it.lyric)
            val buffer = StringBuffer()
            while (matcher.find()) {
                val m1 = matcher.group(1)!!.toInt()
                matcher.appendReplacement(buffer, m1.toChar().toString())
            }
            return@setTransform LyricsAnalysis(buffer.toString()).lyricsList
        }
    }

    /**
     * 还未找到获取pic的方法
     */
    override fun requestPic(musicId: String): Transform<String> {
        return Transform(true, ErrorCode.CODE_OK, "")
    }

    override fun requestPath(musicId: String): Transform<String> {
        val mid = musicId.substring(musicId.indexOf('?') + 1)
        val url =
            "https://u.y.qq.com/cgi-bin/musicu.fcg?-=getplaysongvkey7147489900794954&g_tk=5381&loginUin=0&hostUin=0&format=json&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq.json&needNewCode=0&" +
                    "data={\"req\":{\"module\":\"CDN.SrfCdnDispatchServer\",\"method\":\"GetCdnDispatch\",\"param\":{\"guid\":\"344942144\",\"calltype\":0,\"userip\":\"\"}},\"req_0\":{\"module\":\"vkey.GetVkeyServer\",\"method\":\"CgiGetVkey\"," +
                    "\"param\":{\"guid\":\"344942144\",\"songmid\":[\"$mid\"],\"songtype\":[0],\"uin\":\"0\",\"loginflag\":1,\"platform\":\"20\"}},\"comm\":{\"uin\":0,\"format\":\"json\",\"ct\":24,\"cv\":0}}"

        return http.requestKey(url).setTransform {
            return@setTransform "http://183.60.131.112/amobile.music.tc.qq.com" + it.req0.data.midurlinfo[0].purl
        }

    }
}