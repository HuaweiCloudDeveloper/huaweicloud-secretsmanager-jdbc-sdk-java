package com.huawei.dew.csms.cache;

import com.huawei.dew.csms.model.SecretInfo;
import com.huawei.dew.csms.model.SecretInfoCacheObject;

public interface RefreshStrategy {

    /**
     * 计算下一次轮转时间
     *
     * @param period           凭据轮转间隔
     * @param lastRotationTime 上一次轮转时间
     * @return 下一次轮转时间
     */
    long getNextRotateTime(long period, long lastRotationTime);

    /**
     * 根据凭据缓存获取解析下一次轮转时间
     *
     * @param secretInfoCacheObject
     * @return 下一次轮转时间
     */
    long parseNextRotateTime(SecretInfoCacheObject secretInfoCacheObject);

    /**
     * 根据凭据获取轮转周期
     *
     * @param secretInfo
     * @return 凭据轮转周期，ms
     */
    long parsePeriod(SecretInfo secretInfo);
}
