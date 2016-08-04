package com.tarot.codegen;

import com.mysema.codegen.model.SimpleType;
import com.mysema.codegen.model.Type;
import com.querydsl.codegen.EntityType;

import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.File;
import java.io.Writer;

/**
 * Created by Martin on 2016/8/3.
 */
public class TarotCodeItem {
    public final EntityType entityType;

    public String         packageName;
    public String         simpleName;
    public TypeElement    element;
    public JavaFileObject javaFile;
    public Type           javaType;

    public TarotCodeItem(EntityType entityType) {
        this.entityType = entityType;
    }

    public String getFullName() {
        return packageName + "." + simpleName;
    }

    public Type toSimpleType() {
        javaType = new SimpleType(getFullName(), packageName, simpleName);
        return javaType;
    }

    public boolean fileExists() {
        if (null != javaFile) {
            return new File(javaFile.toUri()).exists();
        }
        return false;
    }
}
