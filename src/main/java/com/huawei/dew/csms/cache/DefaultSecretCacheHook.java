package com.huawei.dew.csms.cache;

import com.huawei.dew.csms.model.SecretInfo;
import com.huawei.dew.csms.model.SecretInfoCacheObject;

public class DefaultSecretCacheHook implements SecretCacheHook {

    private String stage;

    public DefaultSecretCacheHook(String stage) {
        this.stage = stage;
    }

    @Override
    public SecretInfoCacheObject infoToCache(SecretInfo secretInfo) {
        return new SecretInfoCacheObject(secretInfo, stage, System.currentTimeMillis());
    }

    @Override
    public SecretInfo cacheToInfo(SecretInfoCacheObject secretInfoCacheObject) {
        return secretInfoCacheObject.getSecretInfo();
    }
}
