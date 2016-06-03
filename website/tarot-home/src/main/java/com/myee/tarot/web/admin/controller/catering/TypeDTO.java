package com.myee.tarot.web.admin.controller.catering;

import com.myee.tarot.catering.domain.TableType;
import com.myee.tarot.catering.domain.TableZone;

/**
 * Created by Martin on 2016/6/3.
 */
public class TypeDTO {

    private Long   id;
    private String name;

    public TypeDTO() {
    }

    public TypeDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public TypeDTO(TableType type) {
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
