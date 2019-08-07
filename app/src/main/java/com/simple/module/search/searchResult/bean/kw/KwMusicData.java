/**
  * Copyright 2019 bejson.com 
  */
package com.simple.module.search.searchResult.bean.kw;
import java.util.List;

/**
 * Auto-generated: 2019-07-31 19:13:55
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class KwMusicData {

    private List<KwLrclist> lrclist;
    private KwSongInfo songinfo;
    public void setLrclist(List<KwLrclist> lrclist) {
         this.lrclist = lrclist;
     }
     public List<KwLrclist> getLrclist() {
         return lrclist;
     }

    public void setSonginfo(KwSongInfo songinfo) {
         this.songinfo = songinfo;
     }
     public KwSongInfo getSonginfo() {
         return songinfo;
     }


}