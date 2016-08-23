package com.myee.tarot.apiold.bean;

import java.io.Serializable;

/**
 * INFO: 基础类
 * User: zhangxinglong@rui10.com
 * Date: 15-05-4
 * Time: 上午10:31
 * Version: 1.0
 * History: <p>如果有修改过程，请记录</P>
 */
public class BaseInfo implements Serializable {
    protected Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
