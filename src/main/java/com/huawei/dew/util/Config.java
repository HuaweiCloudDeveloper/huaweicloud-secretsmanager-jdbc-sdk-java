package com.huawei.dew.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class Config {
    public static final String CONFIG_FILE = "csms.properties";

    public Properties config;

    public Config() {
    }

    public Config(Properties config) {
        this.config = config;
    }

    public static Config loadConfig() {
        Properties configFromFile = new Properties(System.getProperties());
        try (
                InputStream configInput = Thread.currentThread().getContextClassLoader().getResourceAsStream(CONFIG_FILE);
        ) {
            if (configInput != null) {
                configFromFile.load(configInput);
            }
        } catch (IOException e) {
            throw new WrappedException("load config failed.");
        }
        return new Config(configFromFile);
    }

    public String getStringPropertyWithDefault(String propertyName, String defaultString) {
        String property = config.getProperty(propertyName);
        if (property == null) {
            return defaultString;
        } else {
            return property;
        }
    }

    public int getIntPropertyWithDefault(String propertyName, int defaultInt) {
        String propertyStr = config.getProperty(propertyName);
        if (propertyStr == null) {
            return defaultInt;
        } else {
            return Integer.parseInt(propertyStr);
        }
    }
}
