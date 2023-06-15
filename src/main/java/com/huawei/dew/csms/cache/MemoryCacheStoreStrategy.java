package com.huawei.dew.csms.cache;

import com.huawei.dew.csms.model.SecretInfoCache;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryCacheStoreStrategy implements CacheStoreStrategy {

    /**
     * map键值对缓存，key为凭据名，value为凭据缓存对象
     */
    private final Map<String, SecretInfoCache> secretInfoCacheMap = new ConcurrentHashMap<>();

    @Override
    public void init() {
        //内存缓存无需初始化配置
    }

    @Override
    public void storeSecret(SecretInfoCache secretInfoCache) {
        secretInfoCacheMap.put(secretInfoCache.getSecretInfo().getName(),secretInfoCache);
    }

    @Override
    public SecretInfoCache getSecretInfoCache(String secretName) {
        return secretInfoCacheMap.get(secretName);
    }

    @Override
    public void close() throws IOException {
        if(null != secretInfoCacheMap){
            secretInfoCacheMap.clear();
        }
    }
}
