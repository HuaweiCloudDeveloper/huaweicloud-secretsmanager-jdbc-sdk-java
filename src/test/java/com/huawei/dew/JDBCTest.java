package com.huawei.dew;

import com.huawei.dew.driver.HWSecretsManagerMysqlDriver;
import com.huawei.dew.util.WrappedException;
import org.junit.Test;

import java.sql.*;
import java.util.Properties;

public class JDBCTest {
    @Test
    public void testMysqlDriver() {
        HWSecretsManagerMysqlDriver driver = new HWSecretsManagerMysqlDriver();
        Properties info = new Properties();
        info.put("user", "localdb");
        try (
                Connection connect = driver.connect("jdbc-csms:mysql://localhost:3306/kmsdb?useUnicode=true&characterEncoding=UTF-8", info);
                Statement statement = connect.createStatement();
                ResultSet rs = statement.executeQuery("select * from kekinfo")
        ) {
            while (rs.next()) {
                String uuid = rs.getString(1);
                int state = rs.getInt(2);
                String alias = rs.getString(4);
                System.out.printf("uuid: %s, state: %d, alias: %s \n", uuid, state, alias);
            }
        } catch (SQLException e) {
            throw new WrappedException(e);
        }
    }
}
