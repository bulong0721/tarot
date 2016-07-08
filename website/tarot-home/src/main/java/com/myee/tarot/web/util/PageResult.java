package com.myee.tarot.web.util;

import java.io.Serializable;
import java.util.Collection;

public class PageResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private long          draw;
    private long          recordsTotal;
    private long          recordsFiltered;
    private String        error;
    private Collection<T> rows;

    public PageResult(int draw, String error) {
        this.draw = draw;
        this.error = error;
    }

    public PageResult(long draw, Collection<T> rows) {
        this(draw, rows.size(), rows);
    }

    public PageResult(long draw, long recordsTotal, Collection<T> rows) {
        this(draw, recordsTotal, recordsTotal, rows);
    }

    public PageResult(long draw, long recordsTotal, long recordsFiltered, Collection<T> rows) {
        this.draw = draw;
        this.recordsTotal = recordsTotal;
        this.recordsFiltered = recordsFiltered;
        this.rows = rows;
    }

    public long getDraw() {
        return draw;
    }

    public long getRecordsTotal() {
        return recordsTotal;
    }

    public long getRecordsFiltered() {
        return recordsFiltered;
    }

    public String getError() {
        return error;
    }

    public Collection<T> getRows() {
        return rows;
    }
}
