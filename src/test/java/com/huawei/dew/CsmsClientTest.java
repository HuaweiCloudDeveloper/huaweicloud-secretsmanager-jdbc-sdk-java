package com.huawei.dew;

import com.huawei.dew.csms.client.SecretsManagerCacheClient;
import com.huawei.dew.csms.client.SecretsManagerCacheClientBuilder;
import com.huawei.dew.csms.model.SecretInfo;
import org.junit.Test;

public class CsmsClientTest {
    @Test
    public void testClient(){
        SecretsManagerCacheClient secretsManagerCacheClient = SecretsManagerCacheClientBuilder.getClient();
        SecretInfo secretInfo = secretsManagerCacheClient.getSecretInfo("mysql-test");

        System.out.println(secretInfo.toString());
    }
}
