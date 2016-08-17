package com.myee.tarot.web.catering.controller;

import com.myee.tarot.catering.domain.TableZone;

/**
 * Created by Martin on 2016/6/3.
 */
public class ZoneDTO {

    private Long   id;
    private String name;

    public ZoneDTO() {
    }

    public ZoneDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public ZoneDTO(TableZone zone) {
        this(zone.getId(), zone.getName());
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
