package com.huawei.dew.driver;

import com.huawei.dew.csms.client.SecretsManagerCacheClient;

import java.sql.SQLException;

public class HWSecretsManagerMSSQLServerDriver extends HWSecretsManagerDriver {
    public static final int LOGIN_FAILED = 1045;

    public static final String SUBPREFIX = "sqlserver";

    static {
        HWSecretsManagerMSSQLServerDriver.registerDriver(new HWSecretsManagerMSSQLServerDriver());
    }

    public HWSecretsManagerMSSQLServerDriver() {
        super();
    }

    public HWSecretsManagerMSSQLServerDriver(SecretsManagerCacheClient secretsManagerCacheClient) {
        super(secretsManagerCacheClient);
    }

    @Override
    protected String getRealClass() {
        return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    }

    @Override
    public String getPropertySubPrefix() {
        return SUBPREFIX;
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
