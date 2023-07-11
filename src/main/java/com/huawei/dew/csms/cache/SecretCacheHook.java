package com.huawei.dew.csms.cache;

import com.huawei.dew.csms.model.SecretInfo;
import com.huawei.dew.csms.model.SecretInfoCache;

public interface SecretCacheHook {

    /**
     * 将凭据转换为凭据缓存
     *
     * @param secretInfo
     * @return 凭据缓存
     */
    SecretInfoCache infoToCache(SecretInfo secretInfo);

    /**
     * 将凭据缓存转换成凭据
     *
     * @param secretInfoCache
     * @return 凭据
     */
    SecretInfo cacheToInfo(SecretInfoCache secretInfoCache);
}
