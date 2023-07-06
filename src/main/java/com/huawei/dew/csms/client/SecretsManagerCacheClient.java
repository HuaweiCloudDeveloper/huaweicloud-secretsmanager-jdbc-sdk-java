package com.huawei.dew.csms.client;

import com.huawei.dew.csms.cache.SecretCacheHook;
import com.huawei.dew.csms.model.SecretInfo;
import com.huawei.dew.csms.cache.CacheStoreStrategy;
import com.huawei.dew.csms.cache.RefreshStrategy;
import com.huawei.dew.csms.model.SecretInfoCache;
import com.huawei.dew.util.Constants;
import com.huawei.dew.util.WrappedException;
import com.huaweicloud.sdk.core.utils.StringUtils;
import com.huaweicloud.sdk.csms.v1.CsmsClient;
import com.huaweicloud.sdk.csms.v1.model.*;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

public class SecretsManagerCacheClient implements Closeable {

    private static final long DEFAULT_TTL = 60 * 60 * 1000;

    private final ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(5);
    private final Map<String, ScheduledFuture> scheduledFutureMap = new ConcurrentHashMap<>();
    private final Map<String, Long> nextExecuteTimeMap = new ConcurrentHashMap<>();

    protected SecretsManagerClientBuilder secretsManagerClientBuilder;

    protected CsmsClient csmsClient;

    protected CacheStoreStrategy cacheStoreStrategy;

    protected RefreshStrategy refreshStrategy;

    protected SecretCacheHook secretCacheHook;

    protected Map<String, Long> secretTTLMap = new HashMap<>();

    protected void init() {
        cacheStoreStrategy.init();
        for (String secretName : secretTTLMap.keySet()) {
            SecretInfo secretInfo = getSecretInfo(secretName);
            refresh(secretName, secretInfo);
        }
        new Thread(new MonitorRefreshTask()).start();
    }

    /**
     * 根据凭据名获取凭据信息
     *
     * @param secretName
     * @return
     */
    public SecretInfo getSecretInfo(String secretName) {
        if (StringUtils.isEmpty(secretName)) {
            throw new IllegalArgumentException("secretName must not be null!");
        }
        SecretInfoCache secretInfoCache = this.cacheStoreStrategy.getSecretInfoCache(secretName);
        if (checkSecretInfoCache(secretInfoCache)) {
            return secretCacheHook.getInfo(secretInfoCache);
        } else {

            synchronized (secretName.intern()) {
                SecretInfo secretInfo = getSecretByAPI(secretName);
                refresh(secretName, secretInfo);
                return secretInfo;
            }
        }
    }

    private Boolean checkSecretInfoCache(SecretInfoCache secretInfoCache) {
        if (secretInfoCache == null) {
            return false;
        }
        SecretInfo secretInfo = secretInfoCache.getSecretInfo();
        if (null == secretInfo || StringUtils.isEmpty(secretInfo.getValue())) {
            return false;
        }

        long period = refreshStrategy.parsePeriod(secretInfo);
        if (period < 0) {
            period = secretTTLMap.getOrDefault(secretInfo.getName(), DEFAULT_TTL);
        }

        if (System.currentTimeMillis() - secretInfoCache.getRefreshTimeStamp() <= period) {
            return false;
        }
        return true;
    }

    private SecretInfo getSecretByAPI(String secretName) {

        ShowSecretRequest showSecretRequest = new ShowSecretRequest().withSecretName(secretName);
        ShowSecretStageRequest showSecretStageRequest = new ShowSecretStageRequest().withSecretName(secretName).withStageName("SYSCURRENT");
        ShowSecretResponse secretResponse = csmsClient.showSecret(showSecretRequest);
        ShowSecretStageResponse secretStageResponse = csmsClient.showSecretStage(showSecretStageRequest);
        Secret secret = secretResponse.getSecret();
        Stage stage = secretStageResponse.getStage();
        String versionId = stage.getVersionId();

        ShowSecretVersionRequest showSecretVersionRequest = new ShowSecretVersionRequest().withSecretName(secretName).withVersionId(versionId);
        ShowSecretVersionResponse secretVersionResponse = csmsClient.showSecretVersion(showSecretVersionRequest);
        Version versionInfo = secretVersionResponse.getVersion();

        return new SecretInfo(secret.getId(), secretName, versionId, versionInfo.getSecretString(), Constants.COMMON_TYPE, versionInfo.getVersionMetadata().getCreateTime()
                , null, null, null, secret.getUpdateTime());
    }

    public Boolean refreshNow(final String secretName) throws InterruptedException {
        if (StringUtils.isEmpty(secretName)) {
            throw new IllegalArgumentException("the argument[secretName] must not be null");
        }
        return refreshNow(secretName, null);
    }

    private Boolean refreshNow(String secretName, SecretInfo secretInfo) {
        boolean result = true;
        if (StringUtils.isEmpty(secretName)) {
            throw new IllegalArgumentException("the argument[secretName] must not be null");
        }
        synchronized (secretName.intern()) {
            try {
                refresh(secretName, secretInfo);
            } catch (Throwable e) {
                result = false;
                System.out.println();
            }
            try {
                removeRefreshTask(secretName);
            } catch (Throwable e) {
                result = false;
                System.out.println();
            }
            try {
                addRefreshTask(secretName, new RefreshTask(secretName));
            } catch (Throwable e) {
                result = false;
                System.out.println();
            }
            return result;
        }
    }

    private void refresh(String secretName, SecretInfo secretInfo) {
        if (secretInfo == null) {
            secretInfo = this.getSecretInfo(secretName);
        }

        SecretInfoCache secretInfoCache = this.secretCacheHook.covertToCache(secretInfo);
        if (secretInfoCache != null) {
            this.cacheStoreStrategy.storeSecret(secretInfoCache);
        }
    }

    private void refreshCacheStore(String secretName, SecretInfo secretInfo) {
        if (null == secretInfo) {
            secretInfo = getSecretByAPI(secretName);
        }
        SecretInfoCache secretInfoCache = secretCacheHook.covertToCache(secretInfo);
        if (null != secretInfoCache) {
            cacheStoreStrategy.storeSecret(secretInfoCache);
        }
        System.out.println("secretName refresh success!");
    }

    private void addRefreshTask(String secretName, Runnable runnable) {
        SecretInfoCache secretInfoCache = cacheStoreStrategy.getSecretInfoCache(secretName);
        long nextRefreshTime = refreshStrategy.parseNextRefreshTime(secretInfoCache);
        if (nextRefreshTime <= 0) {
            long refreshTimeStamp = secretInfoCache.getRefreshTimeStamp();
            nextRefreshTime = refreshStrategy.getNextRefreshTime(secretTTLMap.getOrDefault(secretName, DEFAULT_TTL), refreshTimeStamp);
            nextRefreshTime = Math.max(nextRefreshTime, System.currentTimeMillis());
        }

        nextExecuteTimeMap.put(secretName, nextRefreshTime);
        ScheduledFuture<?> schedule = scheduledThreadPoolExecutor.schedule(runnable, nextRefreshTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        scheduledFutureMap.put(secretName, schedule);
        System.out.println("addRefreshTask success!");
    }

    private void removeRefreshTask(String secretName) throws InterruptedException {
        if (scheduledFutureMap.get(secretName) != null) {
            scheduledThreadPoolExecutor.remove((RunnableScheduledFuture<?>) scheduledFutureMap.get(secretName));
        }
    }

    @Override
    public void close() throws IOException {
        if (cacheStoreStrategy != null) {
            cacheStoreStrategy.close();
        }
        if (refreshStrategy != null) {
            refreshStrategy.close();
        }
        if (secretCacheHook != null) {
            secretCacheHook.close();
        }
        if (!scheduledThreadPoolExecutor.isShutdown()) {
            scheduledThreadPoolExecutor.shutdownNow();
        }
    }

    class RefreshTask implements Runnable {
        private final String secretName;

        RefreshTask(String secretName) {
            this.secretName = secretName;
        }

        @Override
        public void run() {
            refreshCacheStore(secretName, null);
            addRefreshTask(secretName, this);
        }
    }

    class MonitorRefreshTask implements Runnable {
        @Override
        public void run() {
            while (true) {
                Set<String> secretNames = nextExecuteTimeMap.keySet();
                if (null != secretNames && !secretNames.isEmpty()) {
                    for (String secretName : secretNames) {
                        SecretInfoCache secretInfoCache = cacheStoreStrategy.getSecretInfoCache(secretName);
                        if (null != secretInfoCache) {
                            Long nextRefreshTime = nextExecuteTimeMap.get(secretName);
                            if (System.currentTimeMillis() > nextRefreshTime + 10 * 60 * 1000L) {
                                try {
                                    refreshNow(secretName);
                                } catch (InterruptedException e) {
                                    throw new WrappedException("Refresh secret failed.");
                                }
                            }
                        } else {
                            try {
                                refreshNow(secretName);
                            } catch (InterruptedException e) {
                                throw new WrappedException("Refresh secret failed.");
                            }
                        }
                    }
                }
                try {
                    Thread.sleep(5 * 60 * 1000);
                } catch (Throwable e) {
                    throw new WrappedException("Thread sleep abnormal.");
                }
            }
        }
    }

}
