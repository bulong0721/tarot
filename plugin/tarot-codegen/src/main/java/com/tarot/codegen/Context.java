package com.tarot.codegen;

import com.google.common.collect.Maps;
import com.mysema.query.codegen.EntityType;

import javax.lang.model.element.TypeElement;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Martin on 2016/8/2.
 */
public class Context {
    final Map<String, EntityType> entityTypes = Maps.newHashMap();

    final Map<String, TypeElement> daoElements = Maps.newHashMap();

    final Map<String, TypeElement> daoImplElements = Maps.newHashMap();

    final Map<String, TypeElement> svcElements = Maps.newHashMap();

    final Map<String, TypeElement> svcImplElements = Maps.newHashMap();

    public void clean() {

        TypeElement element = null;

    }
}
