package com.myee.tarot.log.domain;

/**
 * Created by Ray.Fu on 2016/7/15.
 */
public class SelfCheckLogVO {
    private Long mTime;

    private Integer mEventLevel;

    private Integer mModuleId;

    private Integer mFunctionId;

    private byte[] mData;

    private Integer mLength;

    public Long getmTime() {
        return mTime;
    }

    public void setmTime(Long mTime) {
        this.mTime = mTime;
    }

    public Integer getmEventLevel() {
        return mEventLevel;
    }

    public void setmEventLevel(Integer mEventLevel) {
        this.mEventLevel = mEventLevel;
    }

    public Integer getmModuleId() {
        return mModuleId;
    }

    public void setmModuleId(Integer mModuleId) {
        this.mModuleId = mModuleId;
    }

    public Integer getmFunctionId() {
        return mFunctionId;
    }

    public void setmFunctionId(Integer mFunctionId) {
        this.mFunctionId = mFunctionId;
    }

    public byte[] getmData() {
        return mData;
    }

    public void setmData(byte[] mData) {
        this.mData = mData;
    }

    public Integer getmLength() {
        return mLength;
    }

    public void setmLength(Integer mLength) {
        this.mLength = mLength;
    }
}
