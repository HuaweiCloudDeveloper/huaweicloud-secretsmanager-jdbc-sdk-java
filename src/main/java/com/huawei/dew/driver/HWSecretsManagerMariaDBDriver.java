package com.huawei.dew.driver;

import com.huawei.dew.csms.client.SecretsManagerCacheClient;

import java.sql.SQLException;

public class HWSecretsManagerMariaDBDriver extends HWSecretsManagerDriver {
    public static final int LOGIN_FAILED_CODE = 1045;

    public static final String SUBPREFIX = "mariadb";

    static {
        HWSecretsManagerMariaDBDriver.registerDriver(new HWSecretsManagerMariaDBDriver());
    }

    public HWSecretsManagerMariaDBDriver() {
        super();
    }

    public HWSecretsManagerMariaDBDriver(SecretsManagerCacheClient secretsManagerCacheClient) {
        super(secretsManagerCacheClient);
    }

    @Override
    protected String getRealDriverClass() {
        return "org.mariadb.jdbc.Driver";
    }

    @Override
    public String getPropertySubPrefix() {
        return SUBPREFIX;
    }

    @Override
    protected boolean isAuthError(Exception e) {
        if (e instanceof SQLException) {
            SQLException sqle = (SQLException) e;
            int errorCode = sqle.getErrorCode();
            return errorCode == LOGIN_FAILED_CODE;
        }
        return false;
    }
}
