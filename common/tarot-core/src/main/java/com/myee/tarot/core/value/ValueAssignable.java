package com.myee.tarot.core.value;

import java.io.Serializable;

/**
 * Created by Martin on 2016/4/14.
 */
public interface ValueAssignable<T extends Serializable> extends Serializable {

    /**
     * The value
     *
     * @return The value
     */
    T getValue();

    /**
     * The value
     *
     * @param value The value
     */
    void setValue(T value);

    /**
     * The name
     *
     * @return The name
     */
    String getName();

    /**
     * The name
     *
     * @param name The name
     */
    void setName(String name);
}