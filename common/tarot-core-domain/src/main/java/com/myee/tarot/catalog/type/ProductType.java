package com.myee.tarot.catalog.type;

import com.myee.tarot.core.GenericEnumType;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Martin on 2016/5/23.
 */
public class ProductType implements GenericEnumType, Comparable<ProductType>, Serializable {

    private static final Map<String, ProductType> TYPES = new LinkedHashMap<String, ProductType>();

    private static final ProductType COOKIE = new ProductType("COOKIE", "大学士");

    private String type;
    private String friendlyType;

    public ProductType() {
    }

    public ProductType(String type, String friendlyType) {
        this.friendlyType = friendlyType;
        setType(type);
    }

    @Override
    public int compareTo(ProductType o) {
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

        ProductType that = (ProductType) o;

        return !(type != null ? !type.equals(that.type) : that.type != null);

    }

    @Override
    public int hashCode() {
        return type != null ? type.hashCode() : 0;
    }
}
