package com.huawei.dew;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.huawei.dew.util.WrappedException;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Properties;

import static org.junit.Assert.assertNotNull;

public class DruidTest {

    @Test
    public void testDruidTest() throws Exception {
        Properties properties = new Properties();
        properties.load(DruidTest.class.getResourceAsStream("/druid.properties"));
        DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);
        try (Connection connection = dataSource.getConnection()) {
            assertNotNull(connection);
        } catch (Exception e) {
            throw new WrappedException(e);
        }
    }
}
