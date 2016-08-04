package com.tarot.codegen;

import com.google.common.collect.Maps;
import com.querydsl.codegen.EntityType;

import javax.lang.model.element.TypeElement;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Martin on 2016/8/2.
 */
public class Context {
    final Map<String, EntityType> entityTypes = Maps.newHashMap();

    final Map<String, Set<TypeElement>> typeElements = new HashMap<String,Set<TypeElement>>();

    final Map<String, EntityType> allTypes = new HashMap<String, EntityType>();

    public void clean() {

        TypeElement element = null;

    }
}
