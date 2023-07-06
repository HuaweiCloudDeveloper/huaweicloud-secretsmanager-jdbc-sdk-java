package com.huawei.dew.driver;

import com.huawei.dew.csms.client.SecretCacheClient;

import java.sql.SQLException;

public class HWSecretsManagerMysqlDriver extends HWSecretsManagerDriver {

    public static final int LOGIN_FAILED = 1045;

    public static final String SUBPREFIX = "mysql";

    static {
        HWSecretsManagerMysqlDriver.registerDriver(new HWSecretsManagerMysqlDriver());
    }

    public HWSecretsManagerMysqlDriver() {
        super();
    }

    public HWSecretsManagerMysqlDriver(SecretCacheClient secretCacheClient) {
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
    protected boolean isAuthError(Exception e) {
        if (e instanceof SQLException) {
            SQLException sqle = (SQLException) e;
            int code = sqle.getErrorCode();
            return code == LOGIN_FAILED;
        }
        return false;
    }

}
