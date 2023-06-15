package com.huawei.dew.csms.client;

import com.huawei.dew.csms.cache.*;
import com.huawei.dew.util.Constants;

public class SecretCacheClientBuilder {
    private SecretCacheClient secretCacheClient;

    /**
     * 构建一个SecretCacheClient，不带条件
     *
     * @return SecretCacheClient
     */
    public static SecretCacheClient getClient() {
        SecretCacheClientBuilder builder = new SecretCacheClientBuilder();
        return builder.build();
    }

    /**
     * 设置凭据缓存策略
     *
     * @param cacheStoreStrategy 凭据缓存策略
     * @return SecretCacheClientBuilder
     */
    public SecretCacheClientBuilder withCacheSecretStoreStrategy(CacheStoreStrategy cacheStoreStrategy) {
        buildSecretCacheClient();
        secretCacheClient.cacheStoreStrategy = cacheStoreStrategy;
        return this;
    }

    /**
     * 设置凭据缓存策略
     *
     * @param refreshStrategy 刷新策略
     * @return SecretCacheClientBuilder
     */
    public SecretCacheClientBuilder withRefreshStrategy(RefreshStrategy refreshStrategy) {
        buildSecretCacheClient();
        secretCacheClient.refreshStrategy = refreshStrategy;
        return this;
    }


    public SecretCacheClient build() {
        buildSecretCacheClient();
        if (secretCacheClient.csmsClient == null) {
            secretCacheClient.csmsClient = CsmsClientBuilder.build();
        }
        if (secretCacheClient.cacheStoreStrategy == null) {
            //默认使用内存缓存
            secretCacheClient.cacheStoreStrategy = new MemoryCacheStoreStrategy();
        }
        if(secretCacheClient.refreshStrategy == null){
            secretCacheClient.refreshStrategy = new DefaultRefreshStrategy();
        }
        if(secretCacheClient.secretCacheHook == null){
            secretCacheClient.secretCacheHook = new DefaultSecretCacheHook(Constants.VERSION_STAGE_CURRENT);
        }
        secretCacheClient.init();
        return secretCacheClient;
    }

    private void buildSecretCacheClient() {
        if (null == secretCacheClient) {
            secretCacheClient = new SecretCacheClient();
        }
    }
}
