package com.huawei.dew.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

public final class Config {
    public static final String CONFIG_FILE = "secretmanager.properties";

    private Properties config;

    private String prefix;

    private Config(Properties config, String prefix) {
        this.config = config;
        this.prefix = prefix;
    }

    private static Properties loadConfigFromFile(String fileName) {
        Properties newConfig = new Properties(System.getProperties());
        try (
                InputStream configInput = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
        ) {
            if (configInput != null) {
                newConfig.load(configInput);
            }
        } catch (IOException e) {
            throw new WrappedException("load config failed.");
        }
        return newConfig;
    }

    public static Config loadConfigFile(String resourceName) {
        return new Config(loadConfigFromFile(resourceName), null);
    }

    public static Config loadConfig() {
        return loadConfigFile(CONFIG_FILE);
    }

    public Config getSubconfig(String subPrefix) {
        Enumeration<String> names = (Enumeration<String>) config.propertyNames();
        Properties properties = new Properties();
        while (names.hasMoreElements()) {
            String propertyName = names.nextElement();
            if (isSubproperty(propertyName, subPrefix)) {
                String subPropertyName = getSubproperty(propertyName, subPrefix);
                properties.setProperty(subPropertyName, this.config.getProperty(propertyName));
            }
        }
        if (properties == null) {
            return null;
        } else if (prefix != null) {
            return new Config(properties, prefix + "." + subPrefix);
        } else {
            return new Config(properties, subPrefix);
        }
    }

    private boolean isSubproperty(String propertyName, String subprefix) {
        return propertyName.startsWith(subprefix + ".");
    }

    private String getSubproperty(String fullPropertyName, String subprefix) {
        return fullPropertyName.substring(subprefix.length() + 1);
    }

    public String getStringPropertyWithDefault(String propertyName, String defaultProperty) {
        String property = config.getProperty(propertyName);
        if (property == null) {
            return defaultProperty;
        } else {
            return property;
        }
    }

    public int getIntPropertyWithDefault(String propertyName, int defaultValue) {
        String propertyStr = config.getProperty(propertyName);
        if (propertyStr == null) {
            return defaultValue;
        } else {
            return Integer.parseInt(propertyStr);
        }
    }
}
