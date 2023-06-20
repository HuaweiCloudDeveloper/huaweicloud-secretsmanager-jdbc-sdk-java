package com.huawei.dew.driver;

import com.google.gson.Gson;
import com.huawei.dew.csms.client.SecretCacheClient;
import com.huawei.dew.csms.client.SecretCacheClientBuilder;
import com.huawei.dew.csms.model.SecretInfo;
import com.huawei.dew.util.ConfigUtils;
import com.huawei.dew.util.Constants;
import com.huaweicloud.sdk.core.utils.StringUtils;
import org.apache.commons.lang3.ObjectUtils;

import java.io.IOException;
import java.sql.*;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Logger;

public abstract class HwCsmsDriver implements Driver {

    private static final String SCHEME = "jdbc-csms";

    private static final String PROPERTY_PREFIX = "drivers";

    private String realDriverClass;

    private static final int RETRY_TIMES = 3;

    protected SecretCacheClient secretCacheClient;

    private ConfigUtils configUtils;

    protected abstract String getRealDriverClass();

    protected abstract String contactUrl(String host, String port, String dbName);

    protected abstract boolean isAuthError(Exception e);

    public abstract String getPropertySubPrefix();

    public HwCsmsDriver() {
        this(SecretCacheClientBuilder.getClient());
    }

    public HwCsmsDriver(SecretCacheClient secretCacheClient) {
        this.secretCacheClient = secretCacheClient;

        setConfig();
        registerDriver(this);
    }

    private void setConfig() {
        this.configUtils = ConfigUtils.loadConfig().getSubconfig(PROPERTY_PREFIX + "." + getPropertySubPrefix());
        if (configUtils == null) {
            this.realDriverClass = getRealDriverClass();
            return;
        }
        this.realDriverClass = configUtils.getStringPropertyWithDefault("realDriverClass", getRealDriverClass());
    }

    public static void registerDriver(HwCsmsDriver driver) {
        try {
            DriverManager.registerDriver(driver, () -> shutdown(driver));
        } catch (SQLException sqlException) {
            throw new RuntimeException(String.format("Can not register %s!", driver.getClass().getSimpleName()), sqlException);
        }
    }

    private static void shutdown(HwCsmsDriver driver) {
        try {
            driver.secretCacheClient.close();
        } catch (IOException e) {
            throw new RuntimeException("SecretCacheClient close fail", e);
        }
    }

    @Override
    public Connection connect(String url, Properties info) throws SQLException {

        /**
         * url表示数据库地址，因为我们的凭据保存的RDS信息只有id，并没有地址。
         * info键值对，键为"secretName"，值为凭据名。
         */
        String unWrappedUrl = "";
        String secretName = "";
        if (!acceptsURL(url)) {
            return null;
        } else {
            unWrappedUrl = unWrapUrl(url);
            if (!ObjectUtils.isEmpty(info) && !StringUtils.isEmpty(info.getProperty(Constants.INFO_USER))) {
                secretName = info.getProperty(Constants.INFO_USER);
                return connectWithSecret(unWrappedUrl, secretName);
            } else {
                return getWrappedDriver().connect(unWrappedUrl, info);
            }
        }
    }

    public Connection connectWithSecret(String url, String secretName) throws SQLException {
        int retryTimes = 0;
        Properties userInfo = new Properties();
        while (retryTimes++ <= RETRY_TIMES) {
            try {
                SecretInfo secretInfo = secretCacheClient.getSecretInfo(secretName);
                //获取凭据值
                String secretValue = secretInfo.getValue();
                Properties secretProperties = new Gson().fromJson(secretValue, Properties.class);
                //构建了创建连接时需要的info，包含用户名和密码
                userInfo.put(Constants.INFO_USER, secretProperties.get(Constants.SECRET_USER));
                userInfo.put(Constants.PASSWORD, secretProperties.get(Constants.PASSWORD));
            } catch (Exception e) {
                throw new RuntimeException("Get user info from CSMS fail", e);
            }
            try {
                return getWrappedDriver().connect(url, userInfo);
            } catch (SQLException e) {
                if (isAuthError(e)) {
                    try {
                        if (!secretCacheClient.refreshNow(secretName)) {
                            throw e;
                        }
                    } catch (InterruptedException ex) {
                        throw new RuntimeException("Refresh cache fail", ex);
                    }
                }
                throw e;
            }
        }
        throw new SQLException("Connect times limit exceeded");
    }


    private Driver getWrappedDriver() {
        loadRealDriver();
        Enumeration<Driver> allDrivers = DriverManager.getDrivers();
        while (allDrivers.hasMoreElements()) {
            Driver driver = allDrivers.nextElement();
            if (driver.getClass().getName().equals(getRealDriverClass())) {
                return driver;
            }
        }
        throw new IllegalStateException("get wrapped driver fail.");
    }

    private void loadRealDriver() {
        try {
            Class.forName(getRealDriverClass());
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Could not load real driver with name, \"" + getRealDriverClass() + "\".", e);
        }
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        if (!url.startsWith(SCHEME) || StringUtils.isEmpty(url)) {
            throw new IllegalArgumentException("JDBC URL is malformed. Must use scheme, \"" + SCHEME + "\".");
        }
        return getWrappedDriver().acceptsURL(unWrapUrl(url));
    }

    public String unWrapUrl(String url) {
        if(StringUtils.isEmpty(url)){
            throw new IllegalArgumentException("url is null.");
        }
        return url.replaceFirst(SCHEME, "jdbc");
    }


    @Override
    public int getMajorVersion() {
        return getWrappedDriver().getMajorVersion();
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        return getWrappedDriver().getPropertyInfo(unWrapUrl(url),info);
    }

    @Override
    public boolean jdbcCompliant() {
        return getWrappedDriver().jdbcCompliant();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return getWrappedDriver().getParentLogger();
    }

    @Override
    public int getMinorVersion() {
        return getWrappedDriver().getMinorVersion();
    }


}
