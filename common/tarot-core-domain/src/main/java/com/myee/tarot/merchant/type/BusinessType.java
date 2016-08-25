package com.myee.tarot.merchant.type;

import com.myee.tarot.core.GenericEnumType;

import java.io.Serializable;
import java.util.*;

/**
 * Created by Martin on 2016/5/23.
 */
public class BusinessType implements GenericEnumType, Comparable<BusinessType>, Serializable {

    private static final Map<String, BusinessType> TYPES = new LinkedHashMap<String, BusinessType>();

    private static final BusinessType MARKET = new BusinessType("MARKET", "商场");
    private static final BusinessType FOOD = new BusinessType("FOOD", "餐饮");
    private static final BusinessType RETAIL = new BusinessType("RETAIL", "零售");
    private static final BusinessType OTHER = new BusinessType("OTHER", "其他");
    private static final BusinessType CIRCLE = new BusinessType("CIRCLE", "商圈");

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

    public List getMerchantBusinessType4Select() {
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

    public String getBusinessTypeName(String businessType) {
        try {
            String key = String.valueOf((TYPES.get(businessType)).getFriendlyType());
            return key == null || key.equals("null") ? "" : key;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
