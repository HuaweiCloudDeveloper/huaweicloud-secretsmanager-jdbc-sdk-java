package com.huawei.dew.csms.cache;

import com.huawei.dew.csms.model.SecretInfoCache;

import java.io.Closeable;

public interface CacheStoreStrategy extends Closeable {

    /**
     * 初始化凭据缓存策略
     */
    void init();

    /**
     * 缓存凭据
     */
    void storeSecret(SecretInfoCache secretInfoCache);

    /**
     * 获取缓存凭据缓存
     */
    public SecretInfoCache getSecretInfoCache(String SecretName);

}
