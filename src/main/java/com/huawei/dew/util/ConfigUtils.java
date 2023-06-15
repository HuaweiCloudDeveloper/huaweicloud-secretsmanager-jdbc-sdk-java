package com.huawei.dew.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

public final class ConfigUtils {
    public static final String CONFIG_FILE = "secretmanager.properties";

    private Properties config;

    private String prefix;

    private ConfigUtils(Properties config, String prefix) {
        this.config = config;
        this.prefix = prefix;
    }

    private static Properties loadConfigFromFile(String fileName) {
        Properties newConfig = new Properties(System.getProperties());
        InputStream configFile;

        try {
            configFile = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            if (configFile != null) {
                newConfig.load(configFile);
                configFile.close();
            }
        } catch (IOException e) {
            throw new RuntimeException("loading config from " + CONFIG_FILE + " failed.");
        }
        return newConfig;
    }

    public static ConfigUtils loadConfigFile(String resourceName) {
        return new ConfigUtils(loadConfigFromFile(resourceName), null);
    }

    public static ConfigUtils loadConfig() {
        return loadConfigFile(CONFIG_FILE);
    }

    public ConfigUtils getSubconfig(String subprefix) {
        Enumeration<String> propertyNames = (Enumeration<String>) config.propertyNames();
        Properties subconfig = null;
        while (propertyNames.hasMoreElements()) {
            String name = propertyNames.nextElement();
            if (isSubproperty(name, subprefix)) {
                if (subconfig == null) {
                    subconfig = new Properties();
                }
                String subpropertyName = getSubproperty(name, subprefix);
                subconfig.setProperty(subpropertyName, config.getProperty(name));
            }
        }
        if (subconfig == null) {
            return null;
        } else if (prefix != null) {
            return new ConfigUtils(subconfig, prefix + "." + subprefix);
        } else {
            return new ConfigUtils(subconfig, subprefix);
        }
    }

    private boolean isSubproperty(String propertyName, String subprefix) {
        return propertyName.startsWith(subprefix + ".");
    }

    private String getSubproperty(String fullPropertyName, String subprefix) {
        return fullPropertyName.substring(subprefix.length() + 1);
    }

    public String getStringPropertyWithDefault(String propertyName, String defaultValue) {
        String propertyValue = config.getProperty(propertyName);
        if (propertyValue == null) {
            return defaultValue;
        } else {
            return propertyValue;
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
