package com.myee.tarot.core.web.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by Martin on 2016/4/26.
 */
public class MessageUtil implements ApplicationContextAware {
    protected static ApplicationContext applicationContext;

    public static String getMessage(String code) {
        return null;
    }

    public static String getMessage(String code, Object... args) {
        return null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }
}
