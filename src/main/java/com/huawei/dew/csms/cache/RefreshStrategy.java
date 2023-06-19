package com.huawei.dew.csms.cache;

import com.huawei.dew.csms.model.SecretInfo;
import com.huawei.dew.csms.model.SecretInfoCache;

import java.io.Closeable;

public interface RefreshStrategy extends Closeable {

    /**
     * 计算下一次轮转时间
     *
     * @param period           凭据轮转间隔
     * @param lastRotationTime 上一次轮转时间
     * @return 下一次轮转时间
     */
    long getNextRefreshTime(long period, long lastRotationTime);

    /**
     * 根据凭据缓存获取解析下一次轮转时间
     *
     * @param secretInfoCache
     * @return 下一次轮转时间
     */
    long parseNextRefreshTime(SecretInfoCache secretInfoCache);

    /**
     * 根据凭据获取轮转周期
     *
     * @param secretInfo
     * @return 凭据轮转周期，ms
     */
    long parsePeriod(SecretInfo secretInfo);
}
