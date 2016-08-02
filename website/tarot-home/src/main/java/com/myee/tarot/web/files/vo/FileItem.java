package com.myee.tarot.web.files.vo;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class FileItem implements Serializable{
    private static final int DIR  = 0;
    private static final int FILE = 1;

    private String name;
    private String path;
    private Long salt;
    private String url;
    private Long storeId;
    private String content;
    private long   modified;
    private Long   size;
    private int    type;
    private String currPath;

    private List<FileItem> children;

    public FileItem() {
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Long getSalt() {
        return salt;
    }

    public void setSalt(Long salt) {
        this.salt = salt;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
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

    public long getModified() {
        return modified;
    }

    public void setModified(long modified) {
        this.modified = modified;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public List<FileItem> getChildren() {
        return children;
    }

    public void setChildren(List<FileItem> children) {
        this.children = children;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrPath() {
        return currPath;
    }

    public void setCurrPath(String currPath) {
        this.currPath = currPath;
    }

    public static FileItem toResourceModel(File file, Long salt, Long storeId) {
        FileItem resVo = new FileItem();
        resVo.setName(file.getName());
        resVo.setSalt(salt);
        resVo.setStoreId(storeId);
        resVo.setPath(file.getAbsolutePath());
        resVo.setModified(file.lastModified());
        resVo.type = file.isDirectory() ? DIR : FILE;
        resVo.setSize(file.length());
        return resVo;
    }

}
