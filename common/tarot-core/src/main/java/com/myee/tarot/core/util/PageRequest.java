package com.myee.tarot.core.util;

/**
 * Created by Administrator on 2016/6/6.
 */
public class PageRequest {

    private int count;

    private int page;

    private int start;

    private int length;

    private String queryName;

    private int ifPagint;//是否需要分页显示list，0分页，1不分页

    public int getIfPagint() {
        return ifPagint;
    }

    public void setIfPagint(int ifPagint) {
        this.ifPagint = ifPagint;
    }

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
        this.queryName = queryName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getOffset() {
        if (page > 0) {
            return (page - 1) * count;
        }
        return 0;
    }
}
