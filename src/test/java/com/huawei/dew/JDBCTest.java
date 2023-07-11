package com.huawei.dew;

import com.huawei.dew.driver.HWCsmsMysqlDriver;
import com.huawei.dew.util.WrappedException;
import org.junit.Test;

import java.sql.*;
import java.util.Properties;

import static org.junit.Assert.assertNotNull;

public class JDBCTest {
    @Test
    public void testMysqlDriver() {
        HWCsmsMysqlDriver driver = new HWCsmsMysqlDriver();
        Properties info = new Properties();
        info.put("user", "localdb");
        try (Connection connect = driver.connect("jdbc-csms:mysql://localhost:3306/kmsdb?useUnicode=true&characterEncoding=UTF-8", info)) {
            assertNotNull(connect);
        } catch (SQLException e) {
            throw new WrappedException(e);
        }
    }
}
