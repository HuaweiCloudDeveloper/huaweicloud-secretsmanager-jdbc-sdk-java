package com.huawei.dew.csms.client;

import com.huawei.dew.csms.cache.CacheStoreStrategy;
import com.huawei.dew.csms.cache.DefaultRefreshStrategy;
import com.huawei.dew.csms.cache.DefaultSecretCacheHook;
import com.huawei.dew.csms.cache.MemoryCacheStoreStrategy;
import com.huawei.dew.csms.cache.RefreshStrategy;
import com.huawei.dew.util.Constants;
import org.apache.commons.lang3.ObjectUtils;

public class CsmsCacheClientBuilder {
    private CsmsCacheClient csmsCacheClient;

    /**
     * 构建一个SecretCacheClient，不带条件
     *
     * @return SecretCacheClient
     */
    public static CsmsCacheClient getClient() {
        CsmsCacheClientBuilder builder = new CsmsCacheClientBuilder();
        return builder.build();
    }

    /**
     * 设置凭据缓存策略
     *
     * @param cacheStoreStrategy 凭据缓存策略
     * @return SecretCacheClientBuilder
     */
    public CsmsCacheClientBuilder withCacheSecretStoreStrategy(CacheStoreStrategy cacheStoreStrategy) {
        buildSecretCacheClient();
        csmsCacheClient.cacheStoreStrategy = cacheStoreStrategy;
        return this;
    }

    /**
     * 设置凭据缓存策略
     *
     * @param refreshStrategy 刷新策略
     * @return SecretCacheClientBuilder
     */
    public CsmsCacheClientBuilder withRefreshStrategy(RefreshStrategy refreshStrategy) {
        buildSecretCacheClient();
        csmsCacheClient.refreshStrategy = refreshStrategy;
        return this;
    }


    public CsmsCacheClient build() {
        buildSecretCacheClient();
        if (csmsCacheClient.csmsClient == null) {
            csmsCacheClient.csmsClient = CsmsClientBuilder.build();
        }
        if (csmsCacheClient.cacheStoreStrategy == null) {
            csmsCacheClient.cacheStoreStrategy = new MemoryCacheStoreStrategy();
        }
        if (csmsCacheClient.refreshStrategy == null) {
            csmsCacheClient.refreshStrategy = new DefaultRefreshStrategy();
        }
        if (csmsCacheClient.secretCacheHook == null) {
            csmsCacheClient.secretCacheHook = new DefaultSecretCacheHook(Constants.VERSION_STAGE_CURRENT);
        }
        csmsCacheClient.init();
        return csmsCacheClient;
    }

    private void buildSecretCacheClient() {
        if (ObjectUtils.isEmpty(csmsCacheClient)) {
            csmsCacheClient = new CsmsCacheClient();
        }
    }
}
