package com.huawei.dew.driver;

import com.huawei.dew.csms.client.SecretCacheClient;

import java.sql.SQLException;

public class HWCsmsMSSQLServerDriver extends HWCsmsDriver {
    public static final int LOGIN_FAILED = 1045;

    public static final String SUBPREFIX = "sqlserver";

    static {
        HWCsmsMSSQLServerDriver.registerDriver(new HWCsmsMSSQLServerDriver());
    }

    public HWCsmsMSSQLServerDriver() {
        super();
    }

    public HWCsmsMSSQLServerDriver(SecretCacheClient secretCacheClient) {
        super(secretCacheClient);
    }

    @Override
    protected String getRealDriverClass() {
        return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    }

    @Override
    public String getPropertySubPrefix() {
        return SUBPREFIX;
    }

    @Override
    protected String contactUrl(String host, String port, String dbName) {
        return null;
    }

    @Override
    protected boolean isAuthError(Exception e) {
        if (e instanceof SQLException) {
            SQLException sqlException = (SQLException) e;
            return sqlException.getErrorCode() == LOGIN_FAILED;
        }
        return false;
    }
}
