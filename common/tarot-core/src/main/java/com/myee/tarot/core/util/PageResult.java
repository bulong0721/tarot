package com.myee.tarot.core.util;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/6/6.
 */
public class PageResult<T> implements Serializable {

    private long recordsTotal;

    private long recordsFiltered;

    private List<T> list = Lists.newArrayList();

    public long getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(long recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public long getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(long recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
