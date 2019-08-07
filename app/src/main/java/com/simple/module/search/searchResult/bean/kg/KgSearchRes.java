package com.simple.module.search.searchResult.bean.kg;

public class KgSearchRes {
    private int error_code;
    private KgSearchData data;

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public KgSearchData getData() {
        return data;
    }

    public void setData(KgSearchData data) {
        this.data = data;
    }
}
