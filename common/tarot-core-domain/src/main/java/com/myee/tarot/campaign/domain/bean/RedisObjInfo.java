package com.myee.tarot.campaign.domain.bean;

import java.io.Serializable;

/**
 * INFO: ${todo}
 * User: zhaokai@mail.qianwang365.com
 * Date: 12/23
 * Time: 10:04
 * Version: 1.0
 * History: <p>如果有修改过程，请记录</P>
 */
public class RedisObjInfo implements Serializable {

    public RedisObjInfo() {
    }

    public RedisObjInfo(String redisKey, String fieldKey) {
        this.redisKey = redisKey;
        this.fieldKey = fieldKey;
    }

    private Object objValue;

    private String redisKey;

    private String fieldKey;

    private String value;

    public RedisObjInfo(String redisKey, String fieldKey, String value) {
        this.redisKey = redisKey;
        this.fieldKey = fieldKey;
        this.value = value;
    }

    public RedisObjInfo tranHsetObj(String redisKey, String fieldKey, Object value) {
        RedisObjInfo redisObjInfo = new RedisObjInfo();
        redisObjInfo.setRedisKey(redisKey);
        redisObjInfo.setFieldKey(fieldKey);
        redisObjInfo.setObjValue(value);
        return redisObjInfo;
    }


    public Object getObjValue() {
        return objValue;
    }

    public void setObjValue(Object objValue) {
        this.objValue = objValue;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRedisKey() {
        return redisKey;
    }

    public void setRedisKey(String redisKey) {
        this.redisKey = redisKey;
    }

    public String getFieldKey() {
        return fieldKey;
    }

    public void setFieldKey(String fieldKey) {
        this.fieldKey = fieldKey;
    }
}
