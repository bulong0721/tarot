package com.myee.tarot.web.files.vo;

import java.io.Serializable;

public class ResourceDTO implements Serializable{

    private String name;
    private String url;

    public ResourceDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
