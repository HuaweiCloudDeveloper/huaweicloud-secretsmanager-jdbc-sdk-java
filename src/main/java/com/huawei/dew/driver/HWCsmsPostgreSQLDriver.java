package com.huawei.dew.driver;

import com.huawei.dew.csms.client.SecretCacheClient;

import java.sql.SQLException;

public class HWCsmsPostgreSQLDriver extends HWCsmsDriver{
    public static final String LOGIN_FAILED_CODE = "28P01";

    public static final String SUBPREFIX = "postgresql";

    static {
        HWCsmsPostgreSQLDriver.registerDriver(new HWCsmsPostgreSQLDriver());
    }

    public HWCsmsPostgreSQLDriver() {
        super();
    }

    public HWCsmsPostgreSQLDriver(SecretCacheClient secretCacheClient) {
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
    protected String contactUrl(String host, String port, String dbName) {
        return null;
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
