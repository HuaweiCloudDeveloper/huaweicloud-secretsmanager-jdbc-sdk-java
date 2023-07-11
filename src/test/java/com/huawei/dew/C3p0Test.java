package com.huawei.dew;

import com.huawei.dew.util.WrappedException;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.junit.Test;


import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertNotNull;

public class C3p0Test {
    @Test
    public void testC3p0() {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        try (Connection connection = dataSource.getConnection()) {
            assertNotNull(connection);
        } catch (SQLException e) {
            throw new WrappedException(e);
        }
    }
}
