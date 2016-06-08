package com.myee.tarot.core.util;

/**
 * Created by Administrator on 2016/6/6.
 */
public class PageRequest {

    private int start;

    private int length;

    private String queryName;

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getQueryName() {
        return queryName;
    }

    public void setQueryName(String queryName) {
        this.queryName = queryName ;
    }
}
