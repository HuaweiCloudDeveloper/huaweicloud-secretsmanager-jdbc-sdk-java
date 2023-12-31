package com.huawei.dew.driver;

import com.huawei.dew.csms.client.CsmsCacheClient;

import java.sql.SQLException;

public class HWCsmsPostgreSQLDriver extends HWCsmsDriver {
    public static final String LOGIN_FAILED_CODE = "28P01";

    public HWCsmsPostgreSQLDriver() {
        super();
    }

    public HWCsmsPostgreSQLDriver(CsmsCacheClient csmsCacheClient) {
        super(csmsCacheClient);
    }

    @Override
    protected String getRealClass() {
        return "org.postgresql.Driver";
    }


    @Override
    protected boolean isAuthenticationError(Exception e) {
        if (e instanceof SQLException) {
            String sqlState = ((SQLException) e).getSQLState();
            return sqlState.equals(LOGIN_FAILED_CODE);
        }
        return false;
    }
}
