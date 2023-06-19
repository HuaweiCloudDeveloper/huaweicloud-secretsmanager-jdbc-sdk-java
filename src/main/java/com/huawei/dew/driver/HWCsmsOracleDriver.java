package com.huawei.dew.driver;

import com.huawei.dew.csms.client.SecretCacheClient;

import java.sql.SQLException;

public class HWCsmsOracleDriver extends HWCsmsDriver {
    public static final int USER_CREDENTIALS_DO_NOT_MATCH = 17079;

    public static final int INVALID_USERNAME_OR_PASSWORD = 1017;

    public static final int INCORRECT_USER_PASSWORD = 9911;

    public static final String SUBPREFIX = "oracle";

    static {
        HWCsmsOracleDriver.registerDriver(new HWCsmsOracleDriver());
    }

    public HWCsmsOracleDriver() {
        super();
    }

    public HWCsmsOracleDriver(SecretCacheClient secretCacheClient) {
        super(secretCacheClient);
    }

    @Override
    protected String getRealDriverClass() {
        return "oracle.jdbc.OracleDriver";
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
            SQLException sqle = (SQLException) e;
            int errorCode = sqle.getErrorCode();
            return errorCode == USER_CREDENTIALS_DO_NOT_MATCH
                    || errorCode == INVALID_USERNAME_OR_PASSWORD
                    || errorCode == INCORRECT_USER_PASSWORD;
        }
        return false;
    }
}
