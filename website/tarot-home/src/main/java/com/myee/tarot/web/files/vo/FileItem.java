package com.myee.tarot.web.files.vo;

import com.google.common.collect.Lists;
import com.myee.tarot.web.util.DateTime;

import java.io.File;
import java.util.Date;
import java.util.List;

public class FileItem {
    private static final int DIR  = 0;
    private static final int FILE = 1;

    private String path;
    private String salt;
    private String content;
    private long   modified;
    private Long   size;
    private int    type;

    private List<FileItem> children;

    public FileItem() {
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public static FileItem toResourceModel(File file, Long salt) {
        FileItem resVo = new FileItem();
        resVo.setSalt(Long.toString(salt));
        resVo.setPath(file.getName());
        resVo.setModified(file.lastModified());
        resVo.type = file.isDirectory() ? DIR : FILE;
        resVo.setSize(file.length());
        return resVo;
    }

}
