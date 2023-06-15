package com.huawei.dew;

import com.huawei.dew.csms.client.SecretCacheClient;
import com.huawei.dew.csms.client.SecretCacheClientBuilder;
import com.huawei.dew.csms.model.SecretInfo;
import org.junit.Test;

public class CsmsClientTest {
    @Test
    public void testClient(){
        SecretCacheClient secretCacheClient = SecretCacheClientBuilder.getClient();
        SecretInfo secretInfo = secretCacheClient.getSecretInfo("mysql-test");

        System.out.println(secretInfo.toString());
    }
}
