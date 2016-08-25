package com.myee.tarot.merchant.type;

import com.myee.tarot.core.GenericEnumType;

import java.io.Serializable;
import java.util.*;

/**
 * Created by Martin on 2016/5/23.
 */
public class CuisineType implements GenericEnumType, Comparable<CuisineType>, Serializable {

    private static final Map<String, CuisineType> TYPES = new LinkedHashMap<String, CuisineType>();

    private static final CuisineType CHUAN = new CuisineType("CHUAN", "川菜");
    private static final CuisineType LU = new CuisineType("LU", "鲁菜");
    private static final CuisineType YUE = new CuisineType("YUE", "粤菜");
    private static final CuisineType SU = new CuisineType("SU", "苏菜");
    private static final CuisineType ZHE = new CuisineType("ZHE", "浙菜");
    private static final CuisineType MIN = new CuisineType("MIN", "闽菜");
    private static final CuisineType XIANG = new CuisineType("XIANG", "湘菜");
    private static final CuisineType HUI = new CuisineType("HUI", "徽菜");
    private static final CuisineType CHAOZHOU = new CuisineType("CHAOZHOU", "潮州菜");
    private static final CuisineType DONGBEI = new CuisineType("DONGBEI", "东北菜");
    private static final CuisineType BENBANG = new CuisineType("BENBANG", "本帮菜");
    private static final CuisineType GAN = new CuisineType("GAN", "赣菜");
    private static final CuisineType E = new CuisineType("E", "鄂菜");
    private static final CuisineType JING = new CuisineType("JING", "京菜");
    private static final CuisineType JIN = new CuisineType("JIN", "津菜");
    private static final CuisineType JI = new CuisineType("JI", "冀菜");
    private static final CuisineType YU = new CuisineType("YU", "豫菜");
    private static final CuisineType KEJIA = new CuisineType("KEJIA", "客家菜");
    private static final CuisineType QINGZHEN = new CuisineType("QINGZHEN", "清真菜");
    private static final CuisineType XICAN = new CuisineType("XICAN", "西餐");

    private String type;
    private String friendlyType;

    public CuisineType() {
    }

    public CuisineType(String type, String friendlyType) {
        this.friendlyType = friendlyType;
        setType(type);
    }

    @Override
    public int compareTo(CuisineType o) {
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

        CuisineType that = (CuisineType) o;

        return !(type != null ? !type.equals(that.type) : that.type != null);

    }

    @Override
    public int hashCode() {
        return type != null ? type.hashCode() : 0;
    }

    public List getMerchantCuisine4Select() {
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

    public String getCuisineTypeName(String cuisineType) {
        try {
            String key = String.valueOf((TYPES.get(cuisineType)).getFriendlyType());
            return key == null || key.equals("null") ? "" : key;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
