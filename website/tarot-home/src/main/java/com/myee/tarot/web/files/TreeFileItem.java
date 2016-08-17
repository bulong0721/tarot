package com.myee.tarot.web.files;

import java.util.Date;

/**
 * Created by Administrator on 2016/5/23.
 */
public class TreeFileItem {

    private String id;

    private Boolean children;

    private String text;

    private String type;

    private String icon;

    private Date lastModify;

    private String status;

    private String detailType;

    private String downloadPath;

    public TreeFileItem() {
    }

    public TreeFileItem(String id) {
        this.id = id;
    }

    public String getDetailType() {
        return detailType;
    }

    public void setDetailType(String detailType) {
        this.detailType = detailType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getDownloadPath() {
        return downloadPath;
    }

    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }
}
