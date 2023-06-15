package com.huawei.dew.csms.cache;

import com.huawei.dew.csms.model.SecretInfo;
import com.huawei.dew.csms.model.SecretInfoCache;

import java.io.IOException;

public class DefaultSecretCacheHook implements  SecretCacheHook{

    private String stage;

    public DefaultSecretCacheHook(String stage) {
        this.stage = stage;
    }

    @Override
    public SecretInfoCache covertToCache(SecretInfo secretInfo) {
        //这里为什么新建缓存对象时刷新时间设置为现在？？？
        return new SecretInfoCache(secretInfo,stage,System.currentTimeMillis());
    }

    @Override
    public SecretInfo getInfo(SecretInfoCache secretInfoCache) {
        return secretInfoCache.getSecretInfo();
    }

    @Override
    public void close() throws IOException {

    }
}
