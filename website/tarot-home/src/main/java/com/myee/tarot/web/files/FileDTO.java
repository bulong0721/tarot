package com.myee.tarot.web.files;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * Created by Martin on 2016/5/12.
 */
public class FileDTO {
    private String parent;
    private String name;
    private Long mtime;
    private Long size;
    private int level;
    private boolean leaf;

    public FileDTO() {
    }

    public FileDTO(File target, File root) {
        this.parent = target.getParent();
        this.name = target.getName();
        this.mtime = target.lastModified();
        this.size = target.length();
        this.level = target.toPath().getParent().getNameCount() - root.toPath().getNameCount();
        this.leaf = !target.isDirectory();
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getMtime() {
        return mtime;
    }

    public void setMtime(Long mtime) {
        this.mtime = mtime;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    public boolean isExpanded() {
        return false;
    }
}
