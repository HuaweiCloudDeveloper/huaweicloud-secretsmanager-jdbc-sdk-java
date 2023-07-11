package com.huawei.dew.csms.model;

import java.io.Serializable;

/**
 * 凭据缓存对象
 */
public class SecretInfoCache implements Serializable, Cloneable {

    /**
     * 凭据对象
     */
    private SecretInfo secretInfo;

    /**
     * 凭据版本stage
     */
    private String stage;

    /**
     * 上一次刷新时间戳，ms
     */
    private long refreshTimeStamp;

    public SecretInfoCache() {
    }

    /**
     * 根据指定的凭据，创建凭据缓存。设置凭据版本stage和刷新时间
     *
     * @param secretInfo
     * @param stage
     * @param refreshTimeStamp
     */
    public SecretInfoCache(SecretInfo secretInfo, String stage, long refreshTimeStamp) {
        this.secretInfo = secretInfo;
        this.stage = stage;
        this.refreshTimeStamp = refreshTimeStamp;
    }

    public SecretInfo getSecretInfo() {
        return secretInfo;
    }

    public void setSecretInfo(SecretInfo secretInfo) {
        this.secretInfo = secretInfo;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public long getRefreshTimeStamp() {
        return refreshTimeStamp;
    }

    public void setRefreshTimeStamp(long refreshTimeStamp) {
        this.refreshTimeStamp = refreshTimeStamp;
    }

    @Override
    public SecretInfoCache clone() throws CloneNotSupportedException {
        SecretInfoCache secretInfoCache;
        secretInfoCache = (SecretInfoCache) super.clone();
        secretInfoCache.secretInfo = secretInfo.clone();
        return secretInfoCache;
    }

    @Override
    public String toString() {
        return "SecretInfoCache{" +
                "secretInfo=" + secretInfo +
                ", stage='" + stage + '\'' +
                ", refreshTimeStamp=" + refreshTimeStamp +
                '}';
    }
}
