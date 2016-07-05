package com.myee.tarot.web.admin.controller.product;

import com.myee.tarot.catalog.domain.Device;
import com.myee.tarot.catalog.domain.DeviceAttribute;
import com.myee.tarot.catalog.domain.DeviceUsedAttribute;
import com.myee.tarot.catalog.domain.ProductUsedAttribute;

/**
 * Created by Martin on 2016/6/3.
 */
public class AttributeDTO {

    private Long   id;
    private String name;
    private String value;

    public AttributeDTO() {
    }

    public AttributeDTO(Long id, String name, String value) {
        this.id = id;
        this.name = name;
        this.value = value;
    }

    public AttributeDTO(ProductUsedAttribute attr) {
        this(attr.getId(), attr.getName(), attr.getValue());
    }

    public AttributeDTO(DeviceAttribute attr) {
        this(attr.getId(), attr.getName(), attr.getValue());
    }

    public AttributeDTO(DeviceUsedAttribute attr) {
        this(attr.getId(), attr.getName(), attr.getValue());
    }

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
}
