package com.myee.tarot.campaign.domain.bean;

import java.io.Serializable;

/**
 * INFO: 分页基础类
 * User: zhaokai@mail.qianwang365.com
 * Date: 14-2-2
 * Time: 上午10:05
 * Version: 1.0
 * History: <p>如果有修改过程，请记录</P>
 */

public class Pager implements Serializable {
    // 分页的页码索引.(从1开始计数)
    protected int pageIndex = 1;

    // 每页要求的记录条数.
    protected int pageSize = 10;


    /**
     * @description 详细说明
     */
    public Pager(int pageIndex, int pageSize) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }

    public Pager(int pageSize) {
        this.pageSize = pageSize;
    }

    public Pager() {
    }

    /**
     * @return the pageIndex
     */
    public final int getPageIndex() {
        return pageIndex;
    }

    /**
     * @param pageIndex the pageIndex to set
     */
    public final void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    /**
     * @return the pageSize
     */
    public final int getPageSize() {
        return pageSize;
    }

    /**
     * @param pageSize the pageSize to set
     */
    public final void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }


    public void setParams(String params) {
//        this.params = params;
    }

    public void setActionName(String actionName) {
//        this.actionName = actionName;
    }
}

