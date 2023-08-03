package com.huawei.dew.csms.cache;

import com.google.gson.Gson;
import com.huawei.dew.csms.model.SecretInfo;
import com.huawei.dew.csms.model.SecretInfoCacheObject;

import java.util.Map;

public class DefaultRefreshStrategy implements RefreshStrategy {

    private Gson GSON = new Gson();

    /**
     * 凭据上次轮转时间
     */
    private final static String ROTATION_PERIOD = "rotation_period";

    @Override
    public long getNextRotateTime(long period, long lastRotationTime) {
        long currentTimeMillis = System.currentTimeMillis();
        if (lastRotationTime + period > currentTimeMillis) {
            return lastRotationTime + period;
        }
        return currentTimeMillis + period;
    }

    @Override
    public long parseNextRotateTime(SecretInfoCacheObject secretInfoCacheObject) {
        SecretInfo secretInfo = secretInfoCacheObject.getSecretInfo();
        long period = parsePeriod(secretInfo);
        if (period <= 0) {
            return period;
        }
        return getNextRotateTime(period, secretInfoCacheObject.getRefreshTimeStamp());
    }

    @Override
    public long parsePeriod(SecretInfo secretInfo) {
        try {
            Map<String, Object> map = GSON.fromJson(secretInfo.getValue(), Map.class);
            return ((Double) map.get(ROTATION_PERIOD)).longValue();
        } catch (Exception e) {
            return -1;
        }
    }
}
