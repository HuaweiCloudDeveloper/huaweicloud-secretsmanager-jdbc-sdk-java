package com.huawei.dew.csms.cache;

import com.huawei.dew.csms.model.SecretInfoCacheObject;

/**
 *
 */

public interface CacheStoreStrategy {

    void init();
    /**
     * 缓存凭据
     *
     * @param secretInfoCacheObject 凭据缓存对象
     * @return
     */
    void storeSecret(SecretInfoCacheObject secretInfoCacheObject);

    /**
     * 按凭据名获取缓存中的凭据信息
     *
     * @param secretName 凭据名
     * @return 凭据信息
     */
    public SecretInfoCacheObject getSecretInfoCacheObj(String secretName);

}
