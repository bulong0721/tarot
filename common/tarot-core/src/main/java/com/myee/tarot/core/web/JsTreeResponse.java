package com.myee.tarot.core.web;

import java.util.Date;

/**
 * Created by Administrator on 2016/5/23.
 */
public class JsTreeResponse {

    private String id;

    private Boolean children;

    private String text;

    private String type;

    private String icon;

    private Date lastModify;

    public JsTreeResponse() {
    }

    public JsTreeResponse(String id) {
        this.id = id;
    }

    public Date getLastModify() {
        return lastModify;
    }

    public void setLastModify(Date lastModify) {
        this.lastModify = lastModify;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getChildren() {
        return children;
    }

    public void setChildren(Boolean children) {
        this.children = children;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
