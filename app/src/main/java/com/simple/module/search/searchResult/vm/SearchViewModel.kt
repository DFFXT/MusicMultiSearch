package com.simple.module.search.searchResult.vm

import androidx.lifecycle.MutableLiveData
import com.simple.base.BaseViewModel
import com.simple.base.ifNullOrBlank
import com.simple.bean.Lyrics
import com.simple.bean.Music
import com.simple.bean.SearchMusicRes
import com.simple.module.search.searchResult.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel : BaseViewModel() {
    private val kwModel: KwModel by lazy { KwModel() }
    private val kgModel: KgModel by lazy { KgModel() }
    private val bdModel: BdModel by lazy { BdModel() }
    private val qqModel: QqModel by lazy { QqModel() }
    private val model: ISearchModel
        get() {
            return when (source) {
                Source.KW -> kwModel
                Source.KG -> kgModel
                Source.BD -> bdModel
                Source.QQ -> qqModel
            }
        }
    var source: Source = Source.KW.defSource()
        set(value) {
            if (field == value) return
            field = value
            reset()
        }
    private val searchResultList = SearchMusicRes(0, arrayListOf())

    val liveSearch = MutableLiveData<SearchMusicRes>()
    val liveLrc = MutableLiveData<List<Lyrics>>()
    val livePic = MutableLiveData<String>()
    val livePath = MutableLiveData<String>()
    val status = MutableLiveData<com.simple.module.internet.error.Error?>()
    var page = 1
        private set
    private var pageSize = 20
    private var keyword = ""

    fun reset() {
        page = 1
        searchResultList.data.clear()
        searchResultList.total = 0
    }

    fun search(keyword: String) {
        if (keyword.isEmpty()) return
        if (this.keyword != keyword) {
            reset()
            this.keyword = keyword
        }
        launch(Dispatchers.IO) {
            model.search(keyword, page, pageSize).observer {
                ok { searchMusicRes ->
                    page++
                    searchResultList.data.addAll(searchMusicRes.data)
                    searchMusicRes.total = searchMusicRes.total
                    liveSearch.postValue(searchResultList)
                }
                err {
                    status.postValue(it)
                }
            }
        }
    }

    fun requestFull(music: Music, callback: (Music) -> Unit) {
        launch(Dispatchers.IO) {
            val path = async {
                music.musicPath=music.musicPath.ifNullOrBlank(mRequestPath(music.musicId))
                if(source==Source.KW){
                    if(music.musicPath.startsWith("https://")){
                        music.musicPath.replaceFirst("https://","http://")
                    }else music.musicPath
                }else music.musicPath

            }
            val icon = async {
                music.iconPath.ifNullOrBlank(mRequestPic(music.musicId))
            }
            val lrc = async {
                music.lrc ?: mRequestLrc(music.musicId)
            }
            music.musicPath = path.await()
            music.iconPath = icon.await()
            music.lrc = lrc.await()
            withContext(Dispatchers.Main) {
                callback(music)
            }
        }
    }

    fun requestLrc(id: String) {
        launch(Dispatchers.IO) {
            model.requestLrc(id).observer {
                ok {
                    liveLrc.postValue(it)
                }
            }
        }
    }

    private fun mRequestLrc(id: String): List<Lyrics> {
        return model.requestLrc(id).data!!
    }

    fun requestPic(id: String) {
        launch(Dispatchers.IO) {
            livePic.postValue(mRequestPic(id))
        }
    }

    private fun mRequestPic(id: String): String {
        var res = ""
        model.requestPic(id).observer {
            ok { res = it }
        }
        return res
    }

    fun requestPath(id: String) {
        launch(Dispatchers.IO) {
            livePath.postValue(mRequestPath(id))
        }
    }

    private fun mRequestPath(id: String): String {
        var res = ""
        model.requestPath(id).observer {
            ok { res = it }
        }
        return res
    }

}