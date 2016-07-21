package com.myee.tarot.weixin.domain;


import com.myee.tarot.core.GenericEnumType;

import java.io.Serializable;
import java.util.*;

/**
 * Created by Martin on 2016/1/18.
 */
public class WxWaitTokenState implements GenericEnumType, Comparable<WxWaitTokenState>, Serializable {
    private static final Map<String, WxWaitTokenState> TYPES = new LinkedHashMap<String, WxWaitTokenState>();

    private static final WxWaitTokenState TAKE = new WxWaitTokenState("1", "等待");
    private static final WxWaitTokenState REPAST = new WxWaitTokenState("2", "就餐");
    private static final WxWaitTokenState SKIP = new WxWaitTokenState("3", "过号");
    private static final WxWaitTokenState CANCEL = new WxWaitTokenState("4", "取消");

    private String type;
    private String friendlyType;

    public WxWaitTokenState() {
    }

    public WxWaitTokenState(String type, String friendlyType) {
        this.friendlyType = friendlyType;
        setType(type);
    }

    @Override
    public int compareTo(WxWaitTokenState o) {
        return 0;
    }

    public void setType(final String type) {
        this.type = type;
        if (!TYPES.containsKey(type)) {
            TYPES.put(type, this);
        }
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getFriendlyType() {
        return friendlyType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WxWaitTokenState that = (WxWaitTokenState) o;

        return !(type != null ? !type.equals(that.type) : that.type != null);

    }

    @Override
    public int hashCode() {
        return type != null ? type.hashCode() : 0;
    }

    public List getWxWaitTokenState4Select() {
        List resp = new ArrayList();
        Set keySet = TYPES.keySet();
        for (Object keyName : keySet) {
            Map entry = new HashMap();
            entry.put("name", ((WxWaitTokenState)TYPES.get(keyName)).getFriendlyType());
            entry.put("value", keyName);
            resp.add(entry);
        }
        return resp;
    }

    public String getWxWaitTokenState(String businessType) {
        try {
            String key = String.valueOf(((WxWaitTokenState) TYPES.get(businessType)).getFriendlyType());
            return key == null || key.equals("null") ? "" : key;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
