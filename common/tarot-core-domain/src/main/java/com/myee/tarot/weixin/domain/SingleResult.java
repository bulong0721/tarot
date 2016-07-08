package com.myee.tarot.weixin.domain;

/**
 * Created by Martin on 2016/1/18.
 */
public class SingleResult<T> {
    private String error;
    private T      result;

    public SingleResult(String error) {
        this.error = error;
    }

    public SingleResult(T result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public T getResult() {
        return result;
    }
}
