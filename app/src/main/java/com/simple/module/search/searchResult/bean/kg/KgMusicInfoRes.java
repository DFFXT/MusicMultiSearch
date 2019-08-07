package com.simple.module.search.searchResult.bean.kg;

public class KgMusicInfoRes {
    private int err_code;
    private KgMusicInfoData data;

    public int getErr_code() {
        return err_code;
    }

    public void setErr_code(int err_code) {
        this.err_code = err_code;
    }

    public KgMusicInfoData getData() {
        return data;
    }

    public void setData(KgMusicInfoData data) {
        this.data = data;
    }
}
