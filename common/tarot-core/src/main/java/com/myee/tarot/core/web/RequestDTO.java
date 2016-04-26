package com.myee.tarot.core.web;

/**
 * Created by Martin on 2016/4/14.
 */
public interface RequestDTO {

    String getRequestURI();

    String getFullUrLWithQueryString();

    boolean isSecure();
}
