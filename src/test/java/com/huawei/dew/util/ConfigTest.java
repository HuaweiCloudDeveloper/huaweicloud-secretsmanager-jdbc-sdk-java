package com.huawei.dew.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConfigTest {

    @Test
    public void test_getStringProperty_fromFile() {
        Config config = Config.loadConfig(Constants.CONFIG_FILE);
        String stringProperty = config.getStringPropertyWithDefault("stringProperty", null);
        assertEquals("test", stringProperty);
    }

    @Test
    public void test_getIntProperty_fromFile() {
        Config config = Config.loadConfig(Constants.CONFIG_FILE);
        int intProperty = config.getIntPropertyWithDefault("intProperty", 1);
        assertEquals(1, intProperty);
    }

    @Test
    public void test_getDefault() {
        Config config = Config.loadConfig(Constants.CONFIG_FILE);
        String aDefault = config.getStringPropertyWithDefault("property", "default");
        assertEquals("default", aDefault);
    }

}
