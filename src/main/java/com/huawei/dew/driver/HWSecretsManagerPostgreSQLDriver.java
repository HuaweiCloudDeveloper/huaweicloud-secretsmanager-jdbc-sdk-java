package com.huawei.dew.driver;

import com.huawei.dew.csms.client.SecretCacheClient;

import java.sql.SQLException;

public class HWSecretsManagerPostgreSQLDriver extends HWSecretsManagerDriver {
    public static final String LOGIN_FAILED_CODE = "28P01";

    public static final String SUBPREFIX = "postgresql";

    static {
        HWSecretsManagerPostgreSQLDriver.registerDriver(new HWSecretsManagerPostgreSQLDriver());
    }

    public HWSecretsManagerPostgreSQLDriver() {
        super();
    }

    public HWSecretsManagerPostgreSQLDriver(SecretCacheClient secretCacheClient) {
        super(secretCacheClient);
    }

    @Override
    protected String getRealDriverClass() {
        return "org.postgresql.Driver";
    }

    @Override
    public String getPropertySubPrefix() {
        return SUBPREFIX;
    }

    @Override
    protected boolean isAuthError(Exception e) {
        if (e instanceof SQLException) {
            String sqlState = ((SQLException) e).getSQLState();
            return sqlState.equals(LOGIN_FAILED_CODE);
        }
        return false;
    }
}
