package com.myee.tarot.web.files;

import java.util.Date;
import java.util.Set;

public class HotfixSetVo {
    private String        publisher;
    private Date          timeExpired;
    private String[]      beforeActions;
    private String[]      afterActions;
    private boolean       transactional;
    private Set<HotfixVo> hotfixSet;

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Date getTimeExpired() {
        return timeExpired;
    }

    public void setTimeExpired(Date timeExpired) {
        this.timeExpired = timeExpired;
    }

    public String[] getBeforeActions() {
        return beforeActions;
    }

    public void setBeforeActions(String[] beforeActions) {
        this.beforeActions = beforeActions;
    }

    public String[] getAfterActions() {
        return afterActions;
    }

    public void setAfterActions(String[] afterActions) {
        this.afterActions = afterActions;
    }

    public boolean isTransactional() {
        return transactional;
    }

    public void setTransactional(boolean transactional) {
        this.transactional = transactional;
    }

    public Set<HotfixVo> getHotfixSet() {
        return hotfixSet;
    }

    public void setHotfixSet(Set<HotfixVo> hotfixSet) {
        this.hotfixSet = hotfixSet;
    }
}
