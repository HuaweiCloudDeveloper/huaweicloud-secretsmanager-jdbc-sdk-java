package com.huawei.dew.driver;

import com.huawei.dew.csms.client.SecretsManagerCacheClient;

import java.sql.SQLException;

public class HWSecretsManagerOracleDriver extends HWSecretsManagerDriver {
    public static final int CREDENTIALS_NOT_MATCH = 17079;

    public static final int USERNAME_PASSWORD_INVALID = 1017;

    public static final int USER_PASSWORD_WRONG = 9911;

    public static final String SUBPREFIX = "oracle";

    static {
        HWSecretsManagerOracleDriver.registerDriver(new HWSecretsManagerOracleDriver());
    }

    public HWSecretsManagerOracleDriver() {
        super();
    }

    public HWSecretsManagerOracleDriver(SecretsManagerCacheClient secretsManagerCacheClient) {
        super(secretsManagerCacheClient);
    }

    @Override
    protected String getRealClass() {
        return "oracle.jdbc.OracleDriver";
    }

    @Override
    public String getPropertySubPrefix() {
        return SUBPREFIX;
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
