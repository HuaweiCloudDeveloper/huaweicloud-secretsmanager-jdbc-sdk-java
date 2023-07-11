package com.huawei.dew.driver;

import com.huawei.dew.csms.client.CsmsCacheClient;

import java.sql.SQLException;

public class HWCsmsMysqlDriver extends HWCsmsDriver {

    public static final int LOGIN_FAILED = 1045;

    public static final String SUBPREFIX = "mysql";

    static {
        HWCsmsMysqlDriver.registerDriver(new HWCsmsMysqlDriver());
    }

    public HWCsmsMysqlDriver() {
        super();
    }

    public HWCsmsMysqlDriver(CsmsCacheClient csmsCacheClient) {
        super(csmsCacheClient);
    }

    @Override
    protected String getRealClass() {
        return "com.mysql.cj.jdbc.Driver";
    }

    @Override
    public String getPropertySubPrefix() {
        return SUBPREFIX;
    }

    @Override
    protected boolean isAuthenticationError(Exception e) {
        if (e instanceof SQLException) {
            SQLException sqle = (SQLException) e;
            int code = sqle.getErrorCode();
            return code == LOGIN_FAILED;
        }
        return false;
    }

}
