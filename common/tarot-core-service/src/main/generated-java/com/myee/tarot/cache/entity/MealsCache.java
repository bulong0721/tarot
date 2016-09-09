package com.myee.tarot.cache.entity;

import com.google.common.base.Joiner;
import com.myee.djinn.dto.ShopDetail;
import com.myee.djinn.dto.WaitToken;
import com.myee.tarot.cache.redisson.FastJsonCodec;
import org.redisson.api.*;
import org.redisson.api.annotation.REntity;
import org.redisson.api.annotation.RId;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Martin on 2016/9/6.
 */
@REntity(codec = FastJsonCodec.class)
public class MealsCache implements Serializable {
    @RId
    private String envName;

    private RMapCache<String, String> customerTokenCache;

    private RMapCache<String, WaitToken> waitTokenCache;
}
