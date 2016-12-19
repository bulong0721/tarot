package com.myee.tarot.resource.type;

import com.myee.tarot.core.GenericEnumType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.*;

/**
 * Created by xiaoni on 2016/12/16.
 */
public class UpdateConfigSeeType  implements GenericEnumType, Comparable<UpdateConfigSeeType>, Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateConfigSeeType.class);

    private static final Map<String, UpdateConfigSeeType> TYPES = new LinkedHashMap<String, UpdateConfigSeeType>();

    private static final UpdateConfigSeeType MARKET = new UpdateConfigSeeType("ALL", "全部可见");
    private static final UpdateConfigSeeType FOOD = new UpdateConfigSeeType("CHECKED", "勾选可见");
    private static final UpdateConfigSeeType RETAIL = new UpdateConfigSeeType("NONE", "全不可见");

    private String type;//实际的值
    private String friendlyType;//展示文字

    public UpdateConfigSeeType() {
    }

    public UpdateConfigSeeType(String type, String friendlyType) {
        this.friendlyType = friendlyType;
        setType(type);
    }

    @Override
    public int compareTo(UpdateConfigSeeType o) {
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

        UpdateConfigSeeType that = (UpdateConfigSeeType) o;

        return !(type != null ? !type.equals(that.type) : that.type != null);

    }

    @Override
    public int hashCode() {
        return type != null ? type.hashCode() : 0;
    }

    public List getUpdateConfigSeeType4Select() {
        List resp = new ArrayList();
        Set keySet = TYPES.keySet();
        for (Object keyName : keySet) {
            Map entry = new HashMap();
            entry.put("name", (TYPES.get(keyName)).getFriendlyType());
            entry.put("value", keyName);
            resp.add(entry);
        }
        return resp;
    }

    public String getUpdateConfigSeeTypeName(String typeKey) {
        try {
            String value = String.valueOf((TYPES.get(typeKey)).getFriendlyType());
            return value == null || value.equals("null") ? "" : value;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return "";
        }
    }
}
