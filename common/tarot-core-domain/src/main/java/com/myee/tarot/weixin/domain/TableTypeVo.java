package com.myee.tarot.weixin.domain;

import com.myee.tarot.catering.domain.TableType;

public class TableTypeVo {
    private long   typeId;
    private String typeName;
    private int    capacity;
    private String description;
    private int    minimum;

    public TableTypeVo() {

    }

    public TableTypeVo(TableType type) {
        this(type.getId(), type.getName(), type.getCapacity(), type.getMinimum(), type.getDescription());
    }

    public TableTypeVo(long typeId, String typeName, int capacity, int minimum, String description) {
        super();
        this.typeId = typeId;
        this.typeName = typeName;
        this.capacity = capacity;
        this.minimum = minimum;
        this.description = description;
    }

    public long getTypeId() {
        return typeId;
    }

    public void setTypeId(long typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMinimum() {
        return minimum;
    }

    public void setMinimum(int minimum) {
        this.minimum = minimum;
    }
}