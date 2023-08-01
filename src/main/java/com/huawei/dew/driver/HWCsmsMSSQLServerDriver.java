package com.huawei.dew.driver;

import com.huawei.dew.csms.client.CsmsCacheClient;

import java.sql.SQLException;

public class HWCsmsMSSQLServerDriver extends HWCsmsDriver {
    public static final int LOGIN_FAILED = 1045;

    public HWCsmsMSSQLServerDriver() {
        super();
    }

    public HWCsmsMSSQLServerDriver(CsmsCacheClient csmsCacheClient) {
        super(csmsCacheClient);
    }

    @Override
    protected String getRealClass() {
        return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    }


    @Override
    protected boolean isAuthenticationError(Exception e) {
        if (e instanceof SQLException) {
            SQLException sqlException = (SQLException) e;
            return sqlException.getErrorCode() == LOGIN_FAILED;
        }
        return false;
    }
}
