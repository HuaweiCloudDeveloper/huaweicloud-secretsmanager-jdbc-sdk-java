package com.huawei.dew.csms.cache;

import com.huawei.dew.csms.model.SecretInfo;
import com.huawei.dew.csms.model.SecretInfoCache;

import java.io.Closeable;
import java.io.IOException;

public interface SecretCacheHook extends Closeable {

    /**
     * 将凭据对象转换为凭据缓存对象
     *
     * @param secretInfo
     * @return 凭据缓存对象
     */
    SecretInfoCache covertToCache(SecretInfo secretInfo);

    /**
     * 将凭据缓存对象转换成凭据对象
     *
     * @param secretInfoCache
     * @return 凭据对象
     */
    SecretInfo getInfo(SecretInfoCache secretInfoCache);

    @Override
    void close() throws IOException;
}
