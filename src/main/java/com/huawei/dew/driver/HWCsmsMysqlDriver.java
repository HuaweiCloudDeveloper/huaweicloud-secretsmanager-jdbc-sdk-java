package com.huawei.dew.driver;

import com.huawei.dew.csms.client.SecretCacheClient;

import java.sql.SQLException;

public class HWCsmsMysqlDriver extends HWCsmsDriver {

    public static final int LOGIN_FAILED_CODE = 1045;

    public static final String SUBPREFIX = "mysql";

    static {
        HWCsmsMysqlDriver.registerDriver(new HWCsmsMysqlDriver());
    }

    public HWCsmsMysqlDriver() {
        super();
    }

    public HWCsmsMysqlDriver(SecretCacheClient secretCacheClient) {
        super(secretCacheClient);
    }

    @Override
    protected String getRealDriverClass() {
        return "com.mysql.cj.jdbc.Driver";
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
            return errorCode == LOGIN_FAILED_CODE;
        }
        return false;
    }

}
