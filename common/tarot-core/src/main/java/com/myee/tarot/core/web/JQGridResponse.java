package com.myee.tarot.core.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by Martin on 2016/4/27.
 */
public class JQGridResponse<T> implements Serializable {
    private List<Object> rows = new ArrayList<Object>();
    private long   draw;
    private long   recordsTotal;
    private long   recordsFiltered;
    private String error;

    public List<?> getRows() {
        return rows;
    }

    public void addDataEntry(Map<String, String> dataEntry) {
        this.rows.add(dataEntry);
        this.recordsFiltered = rows.size();
        this.recordsTotal = rows.size();
    }

    public long getDraw() {
        return draw;
    }

    public String getError() {
        return error;
    }

    public long getRecordsFiltered() {
        return recordsFiltered;
    }

    public long getRecordsTotal() {
        return recordsTotal;
    }
}
