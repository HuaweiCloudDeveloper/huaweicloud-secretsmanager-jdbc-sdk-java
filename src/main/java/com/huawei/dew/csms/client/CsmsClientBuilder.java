package com.huawei.dew.csms.client;

import com.huawei.dew.util.Config;
import com.huawei.dew.util.Constants;
import com.huawei.dew.util.WrappedException;
import com.huaweicloud.sdk.core.auth.BasicCredentials;
import com.huaweicloud.sdk.core.auth.ICredential;
import com.huaweicloud.sdk.core.http.HttpConfig;
import com.huaweicloud.sdk.core.utils.StringUtils;
import com.huaweicloud.sdk.csms.v1.CsmsClient;

import java.util.Arrays;
import java.util.List;

public class CsmsClientBuilder {
    private static ICredential credential;

    private static HttpConfig httpConfig;

    private static List<String> KmsEndpointList;


    /**
     * 读取配置文件，初始化参数
     */
    public static void init() {

        Config config = Config.loadConfig();
        if (config == null) {
            throw new WrappedException("loadConfig error. Config is null.");
        }
        credential = new BasicCredentials()
                .withAk(config.getStringPropertyWithDefault(Constants.CREDENTIAL_AK, null))
                .withSk(config.getStringPropertyWithDefault(Constants.CREDENTIAL_SK, null))
                .withSecurityToken(config.getStringPropertyWithDefault(Constants.SECURITY_TOKEN, null))
                .withIdpId(config.getStringPropertyWithDefault(Constants.IDP_ID, null))
                .withIdTokenFile(config.getStringPropertyWithDefault(Constants.ID_TOKEN_FILE, null))
                .withIamEndpoint(config.getStringPropertyWithDefault(Constants.CREDENTIAL_IAM_ENDPOINT, null))
                .withProjectId(config.getStringPropertyWithDefault(Constants.CREDENTIAL_PROJECT_ID, null));

        httpConfig = HttpConfig.getDefaultHttpConfig();

        httpConfig.withProxyHost(config.getStringPropertyWithDefault(Constants.PROXY_HOST, null))
                .withProxyPort(config.getIntPropertyWithDefault(Constants.PROXY_PORT, 8080))
                .withProxyUsername(config.getStringPropertyWithDefault(Constants.PROXY_USER, null))
                .withProxyPassword(config.getStringPropertyWithDefault(Constants.PROXY_PASSWORD, null))
                .withTimeout(config.getIntPropertyWithDefault(Constants.TIMEOUT, 10))
                .withIgnoreSSLVerification("true".equals(config.getStringPropertyWithDefault(Constants.IGNORE_SSL, "fasle")));

        String kmsEndpoints = config.getStringPropertyWithDefault(Constants.KMS_ENDPOINT, null);
        if (!StringUtils.isEmpty(kmsEndpoints)) {
            KmsEndpointList = Arrays.asList(kmsEndpoints.split(","));
        }
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
                .withEndpoints(KmsEndpointList)
                .build();
        return csmsClient;
    }
}
