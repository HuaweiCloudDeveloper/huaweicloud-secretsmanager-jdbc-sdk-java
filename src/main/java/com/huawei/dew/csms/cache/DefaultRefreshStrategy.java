package com.huawei.dew.csms.cache;

import com.google.gson.Gson;
import com.huawei.dew.csms.model.SecretInfo;
import com.huawei.dew.csms.model.SecretInfoCache;

import java.io.IOException;
import java.util.Map;

public class DefaultRefreshStrategy implements RefreshStrategy {

    private final static Gson GSON = new Gson();

    /**
     * 凭据轮转
     */
    private final String rotationTimeName = "rotation_period";

    @Override
    public long getNextRefreshTime(long period, long lastRotationTime) {
        long currentTimeMillis = System.currentTimeMillis();
        if (lastRotationTime + period > currentTimeMillis) {
            return lastRotationTime + period;
        }
        return currentTimeMillis + period;
    }

    @Override
    public long parseNextRefreshTime(SecretInfoCache secretInfoCache) {
        SecretInfo secretInfo = secretInfoCache.getSecretInfo();
        long period = parsePeriod(secretInfo);
        if (period <= 0) {
            return period;
        }
        return getNextRefreshTime(period, secretInfoCache.getRefreshTimeStamp());
    }

    @Override
    public long parsePeriod(SecretInfo secretInfo) {
        try {
            Map<String, Object> map = GSON.fromJson(secretInfo.getValue(), Map.class);
            return ((Double) map.get(rotationTimeName)).longValue();
        } catch (Exception e) {
            return -1;
        }
    }

    @Override
    public void close() throws IOException {

    }
}
