package com.huawei.dew.driver;

import com.huawei.dew.csms.client.CsmsCacheClient;

import java.sql.SQLException;

public class HWCsmsOracleDriver extends HWCsmsDriver {
    public static final int CREDENTIALS_NOT_MATCH = 17079;

    public static final int USERNAME_PASSWORD_INVALID = 1017;

    public static final int USER_PASSWORD_WRONG = 9911;

    public HWCsmsOracleDriver() {
        super();
    }

    public HWCsmsOracleDriver(CsmsCacheClient csmsCacheClient) {
        super(csmsCacheClient);
    }

    @Override
    protected String getRealClass() {
        return "oracle.jdbc.OracleDriver";
    }


    @Override
    protected boolean isAuthenticationError(Exception exception) {
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
