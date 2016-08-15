package com.myee.tarot.web.admin.controller.catering;

import com.myee.tarot.catering.domain.Table;

/**
 * Created by Martin on 2016/6/3.
 */
public class TableDTO {

    private Long   id;
    private String name;

    public TableDTO() {
    }

    public TableDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public TableDTO(Table type) {
        this(type.getId(), type.getName());
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
}
