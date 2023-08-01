package com.huawei.dew.driver;

import com.google.gson.Gson;
import com.huawei.dew.csms.client.CsmsCacheClient;
import com.huawei.dew.csms.client.CsmsCacheClientBuilder;
import com.huawei.dew.csms.model.SecretInfo;
import com.huawei.dew.util.Config;
import com.huawei.dew.util.Constants;
import com.huawei.dew.util.WrappedException;
import com.huaweicloud.sdk.core.utils.StringUtils;
import org.apache.commons.lang3.ObjectUtils;


import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Logger;

public abstract class HWCsmsDriver implements Driver {

    private static final String SCHEME = "jdbc-csms";

    private String realClass;

    private static final int RETRY_TIMES = 3;

    protected CsmsCacheClient csmsCacheClient;

    private Config config;

    protected abstract String getRealClass();

    protected abstract boolean isAuthenticationError(Exception e);


    public HWCsmsDriver() {
        this(CsmsCacheClientBuilder.getClient());
    }

    public HWCsmsDriver(CsmsCacheClient csmsCacheClient) {
        this.csmsCacheClient = csmsCacheClient;

        initConfig();
        registerDriver(this);
    }

    private void initConfig() {
        this.config = Config.loadConfig();
        this.realClass = getRealClass();
    }

    public static void registerDriver(HWCsmsDriver driver) {
        try {
            DriverManager.registerDriver(driver, () -> shutdown(driver));
        } catch (SQLException sqlException) {
            throw new WrappedException("register driver exception, ", sqlException);
        }
    }

    private static void shutdown(HWCsmsDriver driver) {
        try {
            driver.csmsCacheClient.close();
        } catch (IOException e) {
            throw new WrappedException("SecretCacheClient close fail");
        }
    }

    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        if (!acceptsURL(url)) {
            return null;
        } else {
            String unWrappedUrl = unWrapUrl(url);
            if (!ObjectUtils.isEmpty(info) && !StringUtils.isEmpty(info.getProperty(Constants.INFO_USER))) {
                String secretName = info.getProperty(Constants.INFO_USER);
                int retry = 0;
                while (retry++ <= RETRY_TIMES) {
                    Connection connection = connectWithSecret(unWrappedUrl, secretName);
                    if (!ObjectUtils.isEmpty(connection)) {
                        return connection;
                    }
                }
                throw new SQLException("Connect failed.");
            } else {
                return getWrappedDriver().connect(unWrappedUrl, info);
            }
        }
    }

    public Connection connectWithSecret(String url, String secretName) throws SQLException {
        Properties userInfo = new Properties();
        try {
            SecretInfo secretInfo = csmsCacheClient.getSecretInfo(secretName);
            String secretValue = secretInfo.getValue();
            Properties secretProperties = new Gson().fromJson(secretValue, Properties.class);
            userInfo.put(Constants.INFO_USER, secretProperties.get(Constants.SECRET_USER));
            userInfo.put(Constants.PASSWORD, secretProperties.get(Constants.PASSWORD));
        } catch (Exception e) {
            throw new WrappedException("Get user info fail");
        }
        try {
            return getWrappedDriver().connect(url, userInfo);
        } catch (SQLException e) {
            if (isAuthenticationError(e)) {
                try {
                    csmsCacheClient.refreshNow(secretName);
                } catch (InterruptedException ex) {
                    throw new WrappedException("Refresh secrets fail");
                }
            }
            throw e;
        }
    }


    private Driver getWrappedDriver() {
        loadRealDriver();
        Enumeration<Driver> allDrivers = DriverManager.getDrivers();
        while (allDrivers.hasMoreElements()) {
            Driver driver = allDrivers.nextElement();
            if (driver.getClass().getName().equals(realClass)) {
                return driver;
            }
        }
        throw new IllegalStateException("get wrapped driver fail.");
    }

    private void loadRealDriver() {
        try {
            Class.forName(this.realClass);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Could not load real driver with name, \"" + realClass + "\".", e);
        }
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        if (!url.startsWith(SCHEME) || StringUtils.isEmpty(url)) {
            throw new IllegalArgumentException("JDBC URL is invalid. Must be start with \"" + SCHEME + "\".");
        }
        return getWrappedDriver().acceptsURL(unWrapUrl(url));
    }

    public String unWrapUrl(String url) {
        if (StringUtils.isEmpty(url)) {
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
        return getWrappedDriver().getPropertyInfo(unWrapUrl(url), info);
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
