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
    private List<Map<String, String>> rows = new ArrayList<Map<String, String>>();
    private int page;
    private int records;
    private int total;

    private Map<String, Object> userdata;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRecords() {
        return records;
    }

    public void setRecords(int records) {
        this.records = records;
    }

    public List<Map<String, String>> getRows() {
        return rows;
    }

    public void addDataEntry(Map<String, String> dataEntry) {
        this.rows.add(dataEntry);
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Map<String, Object> getUserdata() {
        return userdata;
    }

    public void setUserdata(Map<String, Object> userdata) {
        this.userdata = userdata;
    }
}
