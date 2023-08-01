package com.huawei.dew.driver;

import com.huawei.dew.csms.client.CsmsCacheClient;

import java.sql.SQLException;

public class HWCsmsMariaDBDriver extends HWCsmsDriver {
    public static final int LOGIN_FAILED_CODE = 1045;

    public HWCsmsMariaDBDriver() {
        super();
    }

    public HWCsmsMariaDBDriver(CsmsCacheClient csmsCacheClient) {
        super(csmsCacheClient);
    }

    @Override
    protected String getRealClass() {
        return "org.mariadb.jdbc.Driver";
    }

    @Override
    protected boolean isAuthenticationError(Exception e) {
        if (e instanceof SQLException) {
            SQLException sqle = (SQLException) e;
            int errorCode = sqle.getErrorCode();
            return errorCode == LOGIN_FAILED_CODE;
        }
        return false;
    }
}
