package com.myee.tarot.locale.domain;

import com.myee.tarot.core.GenericEntity;

import javax.persistence.*;

/**
 * Created by Martin on 2016/4/14.
 */
@Entity
@Table(name = "C_LOCALE")
@Cacheable
public class Locale extends GenericEntity<String, Locale> {

    @Id
    @Column(name = "LOCALE_CODE")
    protected String id;

    @Column(name = "FRIENDLY_NAME")
    protected String friendlyName;

    @Column(name = "DEFAULT_FLAG")
    protected boolean defaultFlag = false;

    @Column(name = "USE_IN_SEARCH_INDEX")
    protected boolean useInSearchIndex = false;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public boolean isDefaultFlag() {
        return defaultFlag;
    }

    public void setDefaultFlag(boolean defaultFlag) {
        this.defaultFlag = defaultFlag;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public boolean isUseInSearchIndex() {
        return useInSearchIndex;
    }

    public void setUseInSearchIndex(boolean useInSearchIndex) {
        this.useInSearchIndex = useInSearchIndex;
    }

    public String getLocaleCode() {
        return id;
    }
}
