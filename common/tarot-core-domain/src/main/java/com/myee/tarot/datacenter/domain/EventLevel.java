package com.myee.tarot.datacenter.domain;


import com.myee.tarot.core.GenericEnumType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.*;

/**
 * Created by Martin on 2016/1/18.
 */
public class EventLevel implements GenericEnumType, Comparable<EventLevel>, Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventLevel.class);

    private static final Map<String, EventLevel> TYPES = new LinkedHashMap<String, EventLevel>();

    private static final EventLevel ERROR = new EventLevel("0", "error");
    private static final EventLevel WARN= new EventLevel("1", "warn");
    private static final EventLevel INFO= new EventLevel("2", "info");
    private static final EventLevel DEBUG = new EventLevel("3", "debug");

    private String type;
    private String friendlyType;

    public EventLevel() {
    }

    public EventLevel(String type, String friendlyType) {
        this.friendlyType = friendlyType;
        setType(type);
    }

    @Override
    public int compareTo(EventLevel o) {
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

        EventLevel that = (EventLevel) o;

        return !(type != null ? !type.equals(that.type) : that.type != null);

    }

    @Override
    public int hashCode() {
        return type != null ? type.hashCode() : 0;
    }

    public List getEventLevel4Select() {
        List resp = new ArrayList();
        Set keySet = TYPES.keySet();
        for (Object keyName : keySet) {
            Map entry = new HashMap();
            entry.put("name", ((EventLevel)TYPES.get(keyName)).getFriendlyType());
            entry.put("value", keyName);
            resp.add(entry);
        }
        return resp;
    }

    public String getEventLevel(String businessType) {
        try {
            String key = String.valueOf(((EventLevel) TYPES.get(businessType)).getFriendlyType());
            return key == null || key.equals("null") ? "" : key;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return "";
        }
    }
}
