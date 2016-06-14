package com.myee.tarot.catalog.view;

import com.myee.tarot.catalog.domain.ProductUsed;
import com.myee.tarot.core.GenericEntity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Martin on 2016/4/11.
 */
public class ProductUsedAttributeView implements Serializable {

    private Long id;

    private String name;

    private String value;

    private Long parentId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}
