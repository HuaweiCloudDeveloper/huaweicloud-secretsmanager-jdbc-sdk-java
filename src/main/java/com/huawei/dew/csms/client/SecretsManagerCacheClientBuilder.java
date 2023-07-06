package com.huawei.dew.csms.client;

import com.huawei.dew.csms.cache.*;
import com.huawei.dew.util.Constants;

public class SecretsManagerCacheClientBuilder {
    private SecretsManagerCacheClient secretsManagerCacheClient;

    /**
     * 构建一个SecretCacheClient，不带条件
     *
     * @return SecretCacheClient
     */
    public static SecretsManagerCacheClient getClient() {
        SecretsManagerCacheClientBuilder builder = new SecretsManagerCacheClientBuilder();
        return builder.build();
    }

    /**
     * 设置凭据缓存策略
     *
     * @param cacheStoreStrategy 凭据缓存策略
     * @return SecretCacheClientBuilder
     */
    public SecretsManagerCacheClientBuilder withCacheSecretStoreStrategy(CacheStoreStrategy cacheStoreStrategy) {
        buildSecretCacheClient();
        secretsManagerCacheClient.cacheStoreStrategy = cacheStoreStrategy;
        return this;
    }

    /**
     * 设置凭据缓存策略
     *
     * @param refreshStrategy 刷新策略
     * @return SecretCacheClientBuilder
     */
    public SecretsManagerCacheClientBuilder withRefreshStrategy(RefreshStrategy refreshStrategy) {
        buildSecretCacheClient();
        secretsManagerCacheClient.refreshStrategy = refreshStrategy;
        return this;
    }


    public SecretsManagerCacheClient build() {
        buildSecretCacheClient();
        if (secretsManagerCacheClient.csmsClient == null) {
            secretsManagerCacheClient.csmsClient = SecretsManagerClientBuilder.build();
        }
        if (secretsManagerCacheClient.cacheStoreStrategy == null) {
            secretsManagerCacheClient.cacheStoreStrategy = new MemoryCacheStoreStrategy();
        }
        if (secretsManagerCacheClient.refreshStrategy == null) {
            secretsManagerCacheClient.refreshStrategy = new DefaultRefreshStrategy();
        }
        if (secretsManagerCacheClient.secretCacheHook == null) {
            secretsManagerCacheClient.secretCacheHook = new DefaultSecretCacheHook(Constants.VERSION_STAGE_CURRENT);
        }
        secretsManagerCacheClient.init();
        return secretsManagerCacheClient;
    }

    private void buildSecretCacheClient() {
        if (null == secretsManagerCacheClient) {
            secretsManagerCacheClient = new SecretsManagerCacheClient();
        }
    }
}
