package com.myee.tarot.web.files;

import java.util.Date;
import java.util.List;

/**
 * Created by Martin on 2016/5/12.
 */
public class FileDTO {
    private String        salt;
    private String        name;
    private String        modified;
    private String        extension;
    private String        parent;
    private Long          size;
    private List<FileDTO> children;

    public List<FileDTO> getChildren() {
        return children;
    }

    public void setChildren(List<FileDTO> children) {
        this.children = children;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public boolean isExpanded() {
        return false;
    }

    public int getLevel() {
        return 1;
    }

    public boolean isLeaf() {
        return false;
    }
}
