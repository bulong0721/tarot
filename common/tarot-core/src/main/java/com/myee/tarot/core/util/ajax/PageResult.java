package com.myee.tarot.core.util.ajax;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;

public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    private long              total;
    private List<T>           rows;

    public PageResult(List<T> rows) {
        super();
        this.rows = rows;
        this.total = rows.size();
    }

    public PageResult(List<T> rows, long total) {
        super();
        this.rows = rows;
        this.total = total;
    }

    public void add(T row) {
        if (null == rows) {
            rows = Lists.newArrayList();
        }
        rows.add(row);
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

}
