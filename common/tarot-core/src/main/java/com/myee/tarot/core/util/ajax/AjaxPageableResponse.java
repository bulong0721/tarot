package com.myee.tarot.core.util.ajax;

import java.util.Map;

public class AjaxPageableResponse extends AjaxResponse {
    private long recordsTotal;

    @Override
    public void addDataEntry(Map<String, Object> dataEntry) {
        super.addDataEntry(dataEntry);
        recordsTotal = rows.size();
    }

    public long getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(long recordsTotal) {
        this.recordsTotal = recordsTotal;
    }
}
