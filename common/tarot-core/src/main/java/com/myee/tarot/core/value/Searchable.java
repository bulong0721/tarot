package com.myee.tarot.core.value;

import java.io.Serializable;

/**
 * Created by Martin on 2016/4/14.
 */
public interface Searchable<T extends Serializable> extends ValueAssignable<T> {

    /**
     * Whether or not this class contains searchable information
     *
     * @return Whether or not this class contains searchable information
     */
    boolean isSearchable();

    /**
     * Whether or not this class contains searchable information
     *
     * @param searchable Whether or not this class contains searchable information
     */
    void setSearchable(boolean searchable);

}
