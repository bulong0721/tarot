package com.myee.tarot.merchant.type;

import com.myee.tarot.core.GenericEnumType;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Martin on 2016/5/23.
 */
public class BusinessType implements GenericEnumType, Comparable<BusinessType>, Serializable {

    private static final Map<String, BusinessType> TYPES = new LinkedHashMap<String, BusinessType>();

    private static final BusinessType FOOD = new BusinessType("FOOD", "餐饮");

    private String type;
    private String friendlyType;

    public BusinessType() {
    }

    public BusinessType(String type, String friendlyType) {
        this.friendlyType = friendlyType;
        setType(type);
    }

    @Override
    public int compareTo(BusinessType o) {
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

        BusinessType that = (BusinessType) o;

        return !(type != null ? !type.equals(that.type) : that.type != null);

    }

    @Override
    public int hashCode() {
        return type != null ? type.hashCode() : 0;
    }
}
