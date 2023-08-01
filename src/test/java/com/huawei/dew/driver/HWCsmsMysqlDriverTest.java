package com.huawei.dew.driver;

import com.huawei.dew.csms.client.CsmsCacheClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor("com.huawei.dew.driver.*")
@PowerMockIgnore("javax.*")
public class HWCsmsMysqlDriverTest {
    private HWCsmsMysqlDriver driver;

    @Mock
    private CsmsCacheClient csmsCacheClient;

    @Before
    public void init() {
        System.setProperty("driver.mysql.realDriverClass", "com.huawei.dew.driver.HWCsmsMysqlDriver");
        MockitoAnnotations.initMocks(this);
        try {
            driver = new HWCsmsMysqlDriver(csmsCacheClient);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void test_isAuthenticationError_returnTrue() {
        SQLException sqlException = new SQLException("", "", 1045);

        assertTrue(driver.isAuthenticationError(sqlException));
    }

    @Test
    public void test_isAuthenticationError_returnFalse() {
        SQLException sqlException = new SQLException("", "", 1046);

        assertFalse(driver.isAuthenticationError(sqlException));
    }

    @Test
    public void test_unWrapUrl_rightUrl() throws SQLException {
        String unWrapUrl = driver.unWrapUrl("jdbc-csms:driverClass://endpoint:port/");

        assertEquals(unWrapUrl, "jdbc:driverClass://endpoint:port/");
    }

    @Test
    public void test_getRealClass() {
        String realClass = driver.getRealClass();

        assertEquals(realClass, "com.mysql.cj.jdbc.Driver");
    }

}
