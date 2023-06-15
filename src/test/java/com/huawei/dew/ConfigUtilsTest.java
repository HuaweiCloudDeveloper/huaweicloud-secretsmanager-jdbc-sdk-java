package com.huawei.dew;

import com.huawei.dew.util.ConfigUtils;
import com.huawei.dew.util.Constants;
import org.junit.Test;

public class ConfigUtilsTest {

    @Test
    public void testLoadConfig() {
        ConfigUtils configUtils = ConfigUtils.loadConfig();
        String[] kmsEndpointList = configUtils.getStringPropertyWithDefault(Constants.KMS_ENDPOINT, null).split(",");
        for (int i = 0; i < kmsEndpointList.length; i++) {
            System.out.println(kmsEndpointList[i]);
        }
    }
}
