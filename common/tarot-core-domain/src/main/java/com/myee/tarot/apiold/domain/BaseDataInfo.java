package com.myee.tarot.apiold.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Info: clever
 * User: Gary.zhang@clever-m.com
 * Date: 1/20/16
 * Time: 14:17
 * Version: 1.0
 * History: <p>如果有修改过程，请记录</P>
 */
public class BaseDataInfo implements Serializable {

    private long timestamp;    //更新时间戳
    private List list;
    private List list2;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public List getList2() {
        return list2;
    }

    public void setList2(List list2) {
        this.list2 = list2;
    }
}
