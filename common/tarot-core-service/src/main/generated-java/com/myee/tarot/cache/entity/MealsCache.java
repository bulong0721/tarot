package com.myee.tarot.cache.entity;

import com.myee.tarot.cache.view.WxWaitTokenView;
import org.redisson.api.annotation.REntity;
import org.redisson.api.annotation.RId;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by Martin on 2016/9/6.
 */
@REntity
public class MealsCache implements Serializable {
    @RId
    private String envName;

    private Map<String,List<WxWaitTokenView>> wxWaitTokenCache; //存入redis的排队信息

    private Map<String,String> openIdInfo; //存放微信Id 对应的信息

    private Map<String , Object> deviceUsedMonitor; //存放设备ID  对应的远程监控信息

    public Map<String, String> getOpenIdInfo() {
        return openIdInfo;
    }

    public void setOpenIdInfo(Map<String, String> openIdInfo) {
        this.openIdInfo = openIdInfo;
    }

    public String getEnvName() {
        return envName;
    }

    public void setEnvName(String envName) {
        this.envName = envName;
    }

    public Map<String, List<WxWaitTokenView>> getWxWaitTokenCache() {
        return wxWaitTokenCache;
    }

    public void setWxWaitTokenCache(Map<String, List<WxWaitTokenView>> wxWaitTokenCache) {
        this.wxWaitTokenCache = wxWaitTokenCache;
    }

    public Map<String, Object> getDeviceUsedMonitor() {
        return deviceUsedMonitor;
    }

    public void setDeviceUsedMonitor(Map<String, Object> deviceUsedMonitor) {
        this.deviceUsedMonitor = deviceUsedMonitor;
    }
}
