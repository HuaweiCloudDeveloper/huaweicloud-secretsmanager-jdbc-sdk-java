package com.huawei.dew.driver;

import com.huawei.dew.csms.client.SecretCacheClient;

import java.sql.SQLException;

public class HwCsmsOracleDriver extends HwCsmsDriver {
    public static final int CREDENTIALS_NOT_MATCH = 17079;

    public static final int USERNAME_PASSWORD_INVALID = 1017;

    public static final int USER_PASSWORD_WRONG = 9911;

    public static final String SUBPREFIX = "oracle";

    static {
        HwCsmsOracleDriver.registerDriver(new HwCsmsOracleDriver());
    }

    public HwCsmsOracleDriver() {
        super();
    }

    public HwCsmsOracleDriver(SecretCacheClient secretCacheClient) {
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
    protected boolean isAuthError(Exception exception) {
        if (exception instanceof SQLException) {
            SQLException sqlException = (SQLException) exception;
            int errorCode = sqlException.getErrorCode();
            return errorCode == CREDENTIALS_NOT_MATCH
                    || errorCode == USERNAME_PASSWORD_INVALID
                    || errorCode == USER_PASSWORD_WRONG;
        }
        return false;
    }
}
