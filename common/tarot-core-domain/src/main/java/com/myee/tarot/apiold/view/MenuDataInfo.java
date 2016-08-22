package com.myee.tarot.apiold.view;

import java.util.List;

/**
 * Info: clever
 * User: Gary.zhang@clever-m.com
 * Date: 1/20/16
 * Time: 14:17
 * Version: 1.0
 * History: <p>如果有修改过程，请记录</P>
 */
public class MenuDataInfo {

    private long timestamp;    //更新时间戳
    private boolean enable;    //是否启用餐位费
    private int price;         //餐位费 元/人
    private List list;

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

    public boolean isEnable() {
        return false;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public int getPrice() {
        return 2;
    }

    public void setPrice(int price) {
        this.price = price;
    }


}
