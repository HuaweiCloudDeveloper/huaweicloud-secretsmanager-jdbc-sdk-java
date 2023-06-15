package com.huawei.dew;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.junit.Test;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class C3p0Test {
    @Test
    public void testC3p0() throws Exception {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        Connection connection;
        System.out.println(dataSource.getDriverClass());
        System.out.println(dataSource.getUser());
        System.out.println(dataSource.getJdbcUrl());
        connection = dataSource.getConnection();

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from kekinfo");
        while (resultSet.next()) {
            System.out.println(resultSet.getString("uuid"));
        }
        connection.close();
    }
}
