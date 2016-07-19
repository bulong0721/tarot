package com.myee.tarot.web.log.vo;

/**
 * Created by Administrator on 2016/6/6.
 */
public class SelfCheckLogRequest {

    private int page;

    private int count;

    private String functionName;

    private String moduleName;

    private String errorName;

    private Long storeId;

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
