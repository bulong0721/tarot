package com.tarot.codegen;

import com.querydsl.codegen.EntityType;

/**
 * Created by Martin on 2016/8/1.
 */
public final class TarotCodeChain {
    public final EntityType entity;

    public TarotCodeItem dao;
    public TarotCodeItem daoImpl;
    public TarotCodeItem svc;
    public TarotCodeItem svcImpl;

    public TarotCodeChain(EntityType entity) {
        this.entity = entity;
    }
}
