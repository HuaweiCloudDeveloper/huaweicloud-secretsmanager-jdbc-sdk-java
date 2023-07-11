package com.huawei.dew;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.junit.Test;


import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertNotNull;

public class C3p0Test {
    @Test
    public void testC3p0() throws SQLException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
    }
}
