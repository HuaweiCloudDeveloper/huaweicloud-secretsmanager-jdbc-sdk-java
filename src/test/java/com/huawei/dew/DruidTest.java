package com.huawei.dew;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DruidTest {

    @Test
    public void testDruidTest() {
        Properties properties = new Properties();
        try {
            properties.load(DruidTest.class.getResourceAsStream("/druid.properties"));
            DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from kekinfo");
            while (resultSet.next()) {
                System.out.println(resultSet.getString("uuid"));
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
