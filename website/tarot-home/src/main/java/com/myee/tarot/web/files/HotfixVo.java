package com.myee.tarot.web.files;

public class HotfixVo {
    private String  name;
    private String  url;
    private String  targetDir;
    private boolean compress;

    public HotfixVo() {
    }

    public HotfixVo(String name, String url, String targetDir, boolean compress) {
        super();
        this.name = name;
        this.url = url;
        this.targetDir = targetDir;
        this.compress = compress;
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

    public String getTargetDir() {
        return targetDir;
    }

    public void setTargetDir(String targetDir) {
        this.targetDir = targetDir;
    }

    public boolean isCompress() {
        return compress;
    }

    public void setCompress(boolean compress) {
        this.compress = compress;
    }

}