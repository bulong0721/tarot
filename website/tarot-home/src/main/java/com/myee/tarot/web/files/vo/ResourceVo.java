package com.myee.tarot.web.files.vo;

import com.myee.tarot.web.util.DateTime;

import java.io.File;
import java.util.Date;
import java.util.List;

public class ResourceVo {
    private String           path;
    private String           salt;
    private String           name;
    private String           content;
    private String           modified;
    private int              resType;
    private Long             size;
    private Date             created;
    private List<ResourceVo> children;

    public ResourceVo() {
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String absPath) {
        this.path = absPath;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public int getResType() {
        return resType;
    }

    public void setResType(int resType) {
        this.resType = resType;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public List<ResourceVo> getChildren() {
        return children;
    }

    public void setChildren(List<ResourceVo> children) {
        this.children = children;
    }

    public boolean isLeaf() {
        return resType != 1;
    }

    public boolean isChecked() {
        return false;
    }

    public static ResourceVo toResourceModel(File file, Long salt) {
        ResourceVo resVo = new ResourceVo();
        resVo.setSalt(Long.toString(salt));
        resVo.setPath(file.getAbsolutePath());
        resVo.setName(file.getName());
        resVo.setModified(DateTime.toNormalDateTime(file.lastModified()));
        resVo.setName(file.getName());
        resVo.setResType(file.isDirectory() ? 1 : 2);
        resVo.setSize(file.length());
        return resVo;
    }

}
