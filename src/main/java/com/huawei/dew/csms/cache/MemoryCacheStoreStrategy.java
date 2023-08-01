package com.huawei.dew.csms.cache;

import com.huawei.dew.csms.model.SecretInfoCacheObject;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryCacheStoreStrategy implements CacheStoreStrategy {

    private final Map<String, SecretInfoCacheObject> secretInfoCacheMap = new ConcurrentHashMap<>();

    @Override
    public void init() {
    }

    @Override
    public void storeSecret(SecretInfoCacheObject secretInfoCacheObject) {
        secretInfoCacheMap.put(secretInfoCacheObject.getSecretInfo().getName(), secretInfoCacheObject);
    }

    @Override
    public SecretInfoCacheObject getSecretInfoCacheObj(String secretName) {
        return secretInfoCacheMap.get(secretName);
    }

}
