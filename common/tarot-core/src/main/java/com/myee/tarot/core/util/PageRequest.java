package com.myee.tarot.core.util;

import com.google.common.collect.Maps;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/6.
 */
public class PageRequest implements Serializable {

    private int page;

    private int count;

    private String queryName;

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

    public int getOffset() {
        if (page > 0) {
            return (page - 1) * count;
        }
        return 0;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPage() {
        return page;
    }
}
