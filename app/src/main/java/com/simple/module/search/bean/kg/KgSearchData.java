package com.simple.module.search.bean.kg;

import java.util.List;

public class KgSearchData {
    private int page;
    private String tab;
    private int chinesecount;
    private int searchfull;
    private int correctiontype;
    private int subjecttype;
    private int allowerr;
    private String correctionsubject;
    private int correctionforce;
    private int total;
    private int istagresult;
    private int istag;
    private String correctiontip;
    private int pagesize;
    private List<KgSearchMusic> lists;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getTab() {
        return tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }

    public int getChinesecount() {
        return chinesecount;
    }

    public void setChinesecount(int chinesecount) {
        this.chinesecount = chinesecount;
    }

    public int getSearchfull() {
        return searchfull;
    }

    public void setSearchfull(int searchfull) {
        this.searchfull = searchfull;
    }

    public int getCorrectiontype() {
        return correctiontype;
    }

    public void setCorrectiontype(int correctiontype) {
        this.correctiontype = correctiontype;
    }

    public int getSubjecttype() {
        return subjecttype;
    }

    public void setSubjecttype(int subjecttype) {
        this.subjecttype = subjecttype;
    }

    public int getAllowerr() {
        return allowerr;
    }

    public void setAllowerr(int allowerr) {
        this.allowerr = allowerr;
    }

    public String getCorrectionsubject() {
        return correctionsubject;
    }

    public void setCorrectionsubject(String correctionsubject) {
        this.correctionsubject = correctionsubject;
    }

    public int getCorrectionforce() {
        return correctionforce;
    }

    public void setCorrectionforce(int correctionforce) {
        this.correctionforce = correctionforce;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getIstagresult() {
        return istagresult;
    }

    public void setIstagresult(int istagresult) {
        this.istagresult = istagresult;
    }

    public int getIstag() {
        return istag;
    }

    public void setIstag(int istag) {
        this.istag = istag;
    }

    public String getCorrectiontip() {
        return correctiontip;
    }

    public void setCorrectiontip(String correctiontip) {
        this.correctiontip = correctiontip;
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public List<KgSearchMusic> getLists() {
        return lists;
    }

    public void setLists(List<KgSearchMusic> lists) {
        this.lists = lists;
    }
}
