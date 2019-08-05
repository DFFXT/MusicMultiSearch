package com.simple.module.search.bean.kw;

public class KwSearchMusic {
    private String musicrid;
    private int hasmv;
    private String artist;
    private String releaseDate;
    private String album;
    private long albumid;
    private String pay;
    private long artistid;
    private String albumpic;
    private String songTimeMinutes;
    private String pic;
    private int isstar;
    private long rid;
    private boolean isListenFee;
    private int duration;
    private String pic120;
    private String name;
    private int online;
    private int track;
    private boolean hasLossless;

    public String getMusicrid() {
        return musicrid;
    }

    public void setMusicrid(String musicrid) {
        this.musicrid = musicrid;
    }

    public int getHasmv() {
        return hasmv;
    }

    public void setHasmv(int hasmv) {
        this.hasmv = hasmv;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public long getAlbumid() {
        return albumid;
    }

    public void setAlbumid(long albumid) {
        this.albumid = albumid;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public long getArtistid() {
        return artistid;
    }

    public void setArtistid(long artistid) {
        this.artistid = artistid;
    }

    public String getAlbumpic() {
        return albumpic;
    }

    public void setAlbumpic(String albumpic) {
        this.albumpic = albumpic;
    }

    public String getSongTimeMinutes() {
        return songTimeMinutes;
    }

    public void setSongTimeMinutes(String songTimeMinutes) {
        this.songTimeMinutes = songTimeMinutes;
    }

    public String getPic() {
        return pic==null?"":pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getIsstar() {
        return isstar;
    }

    public void setIsstar(int isstar) {
        this.isstar = isstar;
    }

    public long getRid() {
        return rid;
    }

    public void setRid(long rid) {
        this.rid = rid;
    }

    public boolean isListenFee() {
        return isListenFee;
    }

    public void setListenFee(boolean listenFee) {
        isListenFee = listenFee;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getPic120() {
        return pic120;
    }

    public void setPic120(String pic120) {
        this.pic120 = pic120;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public int getTrack() {
        return track;
    }

    public void setTrack(int track) {
        this.track = track;
    }

    public boolean isHasLossless() {
        return hasLossless;
    }

    public void setHasLossless(boolean hasLossless) {
        this.hasLossless = hasLossless;
    }
}
