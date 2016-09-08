package com.myee.tarot.cache.entity;

import com.myee.tarot.apiold.domain.BaseDataInfo;
import com.myee.tarot.apiold.view.MenuDataInfo;
import com.myee.tarot.cache.redisson.FastJsonCodec;
import org.redisson.api.RMapCache;
import org.redisson.api.annotation.REntity;
import org.redisson.api.annotation.RId;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Martin on 2016/9/6.
 */
@REntity(codec = FastJsonCodec.class)
public class CommonCache implements Serializable {
    @RId
    private String envName;

    private RMapCache<String, MenuDataInfo> menuCache;

    private RMapCache<String, BaseDataInfo> tableCache;

    private RMapCache<String, BaseDataInfo> tableTypeCache;

    private RMapCache<String, BaseDataInfo> tasteCache;

    public String getEnvName() {
        return envName;
    }

    public void setEnvName(String envName) {
        this.envName = envName;
    }

    public RMapCache<String, MenuDataInfo> getMenuCache() {
        return menuCache;
    }

    public void setMenuCache(RMapCache<String, MenuDataInfo> menuCache) {
        this.menuCache = menuCache;
    }

    public RMapCache<String, BaseDataInfo> getTableCache() {
        return tableCache;
    }

    public void setTableCache(RMapCache<String, BaseDataInfo> tableCache) {
        this.tableCache = tableCache;
    }

    public RMapCache<String, BaseDataInfo> getTableTypeCache() {
        return tableTypeCache;
    }

    public void setTableTypeCache(RMapCache<String, BaseDataInfo> tableTypeCache) {
        this.tableTypeCache = tableTypeCache;
    }

    public RMapCache<String, BaseDataInfo> getTasteCache() {
        return tasteCache;
    }

    public void setTasteCache(RMapCache<String, BaseDataInfo> tasteCache) {
        this.tasteCache = tasteCache;
    }
}
