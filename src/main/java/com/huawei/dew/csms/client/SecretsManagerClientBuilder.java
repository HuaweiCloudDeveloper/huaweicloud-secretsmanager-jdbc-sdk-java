package com.huawei.dew.csms.client;

import com.huawei.dew.util.ConfigUtils;
import com.huawei.dew.util.Constants;
import com.huawei.dew.util.WrappedException;
import com.huaweicloud.sdk.core.auth.BasicCredentials;
import com.huaweicloud.sdk.core.auth.ICredential;
import com.huaweicloud.sdk.core.http.HttpConfig;
import com.huaweicloud.sdk.csms.v1.CsmsClient;

import java.util.Arrays;
import java.util.List;

public class SecretsManagerClientBuilder {
    private static ICredential credential;

    private static HttpConfig httpConfig;

    private static List<String> KmsEndpoints;


    /**
     * 读取配置文件，初始化参数
     */
    public static void init() {

        ConfigUtils configUtils = ConfigUtils.loadConfig();
        if (configUtils == null) {
            throw new WrappedException("loadConfig error. Config is null.");
        }
        credential = new BasicCredentials()
                .withAk(configUtils.getStringPropertyWithDefault(Constants.CREDENTIAL_AK, null))
                .withSk(configUtils.getStringPropertyWithDefault(Constants.CREDENTIAL_SK, null))
                .withSecurityToken(configUtils.getStringPropertyWithDefault(Constants.SECURITY_TOKEN, null))
                .withIdpId(configUtils.getStringPropertyWithDefault(Constants.IDP_ID, null))
                .withIdTokenFile(configUtils.getStringPropertyWithDefault(Constants.ID_TOKEN_FILE, null))
                .withIamEndpoint(configUtils.getStringPropertyWithDefault(Constants.CREDENTIAL_IAM_ENDPOINT, null))
                .withProjectId(configUtils.getStringPropertyWithDefault(Constants.CREDENTIAL_PROJECT_ID, null));

        httpConfig = HttpConfig.getDefaultHttpConfig();

        httpConfig.withProxyHost(configUtils.getStringPropertyWithDefault(Constants.PROXY_HOST, null))
                .withProxyPort(configUtils.getIntPropertyWithDefault(Constants.PROXY_PORT, 8080))
                .withProxyUsername(configUtils.getStringPropertyWithDefault(Constants.PROXY_USER, null))
                .withProxyPassword(configUtils.getStringPropertyWithDefault(Constants.PROXY_PASSWORD, null))
                .withTimeout(configUtils.getIntPropertyWithDefault(Constants.TIMEOUT, 10))
                .withIgnoreSSLVerification("true".equals(configUtils.getStringPropertyWithDefault(Constants.IGNORE_SSL, "fasle")));

        KmsEndpoints = Arrays.asList(configUtils.getStringPropertyWithDefault(Constants.KMS_ENDPOINT, null).split(","));
    }

    /**
     * 构建凭据管理客户端
     *
     * @return CsmsClient
     */
    public static CsmsClient build() {
        init();
        CsmsClient csmsClient = CsmsClient.newBuilder()
                .withCredential(credential)
                .withHttpConfig(httpConfig)
                .withEndpoints(KmsEndpoints)
                .build();
        return csmsClient;
    }
}
