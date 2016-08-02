package com.tarot.codegen;

import com.mysema.query.codegen.EntityType;

import javax.lang.model.element.TypeElement;

/**
 * Created by Martin on 2016/8/1.
 */
public final class TarotCodeChain {
    public final EntityType entity;

    public TypeElement dao;
    public TypeElement daoImpl;
    public TypeElement svc;
    public TypeElement svcImpl;

    public TarotCodeChain(EntityType entity) {
        this.entity = entity;
    }
}
