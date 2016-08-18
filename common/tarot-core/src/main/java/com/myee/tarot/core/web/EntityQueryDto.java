package com.myee.tarot.core.web;

/**
 * Created by Ray.Fu on 2016/8/18.
 */
public class EntityQueryDto {

    //存放什么数值
    private Object fieldValue;
    //查询方式(0: must, 1: should)
    private int queryPattern;

    public Object getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(Object fieldValue) {
        this.fieldValue = fieldValue;
    }

    public int getQueryPattern() {
        return queryPattern;
    }

    public void setQueryPattern(int queryPattern) {
        this.queryPattern = queryPattern;
    }
}
