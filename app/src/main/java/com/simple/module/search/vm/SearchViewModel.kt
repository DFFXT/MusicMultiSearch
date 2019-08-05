package com.simple.module.search.vm

import androidx.lifecycle.MutableLiveData
import com.simple.base.BaseViewModle
import com.simple.bean.Lyrics
import com.simple.bean.SearchMusicRes
import com.simple.module.internet.observer
import com.simple.module.search.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel : BaseViewModle() {
    private val kwModel: KwModel by lazy { KwModel() }
    private val kgModel: KgModel by lazy { KgModel() }
    private val bdModel: BdModel by lazy { BdModel() }
    private val qqModel: QqModel by lazy { QqModel() }
    private val model:ISearchModel
        get() {
            return when(source){
                Source.KW-> kwModel
                Source.KG-> kgModel
                Source.BD-> bdModel
                Source.QQ-> qqModel
            }
        }
    var source:Source=Source.KW
    private var page=0
    private val pageSize=30
    private val searchResultList= SearchMusicRes(0, arrayListOf())

    val liveSearch=MutableLiveData<SearchMusicRes>()
    val liveLrc=MutableLiveData<List<Lyrics>>()
    val livePic=MutableLiveData<String>()
    val livePath=MutableLiveData<String>()



    fun search(keyword:String){
        launch (Dispatchers.IO){
            model.search(keyword,page,pageSize).observer({searchMusicRes ->
                searchResultList.data.addAll(searchMusicRes.data)
                searchMusicRes.total=searchMusicRes.total
                liveSearch.postValue(searchResultList)
            })
        }
    }

    fun requestLrc(id:String){
        launch (Dispatchers.IO){
            model.requestLrc(id).observer({
                liveLrc.postValue(it)
            })
        }

    }
    fun requestPic(id:String){
        launch(Dispatchers.IO) {
            model.requestPic(id).observer({
                livePic.postValue(it)
            })
        }
    }
    fun requestPath(id:String){
        launch(Dispatchers.IO) {
            model.requestPath(id).observer({
                livePath.postValue(it)
            })
        }

    }

}