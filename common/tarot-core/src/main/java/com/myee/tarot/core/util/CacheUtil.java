package com.myee.tarot.core.util;

import org.springframework.stereotype.Component;

/**
 * Created by Martin on 2016/4/8.
 */
@Component("cache")
public class CacheUtil {

    public void putInCache(Object object, String keyName) throws Exception {

//        cache.put(keyName, object);

    }

    public Object getFromCache(String keyName) throws Exception {

//        Cache.ValueWrapper vw = cache.get(keyName);
//        if(vw!=null) {
//            return vw.get();
//        }

        return null;

    }

    public void shutDownCache() throws Exception {

    }

    public void removeFromCache(String keyName) throws Exception {
//        cache.evict(keyName);
    }

}
