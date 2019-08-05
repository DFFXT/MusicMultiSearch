package com.simple.module.search.bean.kw;

import java.util.ArrayList;

public class KwSearchData {
    private int total;
    private ArrayList<KwSearchMusic> list;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public ArrayList<KwSearchMusic> getList() {
        return list;
    }

    public void setList(ArrayList<KwSearchMusic> list) {
        this.list = list;
    }
}
