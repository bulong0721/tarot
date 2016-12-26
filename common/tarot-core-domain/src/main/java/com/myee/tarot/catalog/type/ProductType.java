package com.myee.tarot.catalog.type;

import com.myee.tarot.core.GenericEnumType;

import java.io.Serializable;
import java.util.*;

/**
 * Created by Martin on 2016/5/23.
 */
public class ProductType implements GenericEnumType, Comparable<ProductType>, Serializable {

    private static final Map<String, ProductType> TYPES = new LinkedHashMap<String, ProductType>();

    private static final ProductType COOKIE = new ProductType("COOKY", "大学士");
    private static final ProductType LITTLE_GIRL = new ProductType("LITTLE_GIRL", "小女生");
//    private static final ProductType POINT_PEN = new ProductType("POINT_PEN", "美味点点笔");
    private static final ProductType POINT_PEN = new ProductType("POINT_PEN", "小超人");

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

    public static List<ProductType> getProductTypeList(){
        List<ProductType> productTypeList = new ArrayList<ProductType>();
            Iterator it = TYPES.values().iterator();
            while (it.hasNext()) {
                ProductType type = (ProductType)it.next();
                productTypeList.add(type);
            }
        return productTypeList;
    }

    public static List getProductTypeList4Select() {
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

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getFriendlyType() {
        return friendlyType;
    }

    public static String getName(final String type){
        String name = null;
        if (TYPES.containsKey(type)) {
            name = TYPES.get(type).getFriendlyType();
        }
        return name;
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
