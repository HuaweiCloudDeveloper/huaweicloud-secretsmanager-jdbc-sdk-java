package com.huawei.dew;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class DruidTest {

    @Test
    public void testDruidTest() throws Exception {
        Properties properties = new Properties();
        properties.load(DruidTest.class.getResourceAsStream("/druid.properties"));
        DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from kekinfo");
        while (resultSet.next()){
            System.out.println(resultSet.getString("uuid"));
        }
        resultSet.close();
        statement.close();
        connection.close();
    }
}
