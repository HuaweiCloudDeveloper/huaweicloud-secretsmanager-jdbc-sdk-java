package com.huawei.dew.csms.client;

import com.huawei.dew.util.ConfigUtils;
import com.huawei.dew.util.Constants;
import com.huaweicloud.sdk.core.auth.BasicCredentials;
import com.huaweicloud.sdk.core.auth.ICredential;
import com.huaweicloud.sdk.core.http.HttpConfig;
import com.huaweicloud.sdk.csms.v1.CsmsClient;

import java.util.Arrays;
import java.util.List;

public class CsmsClientBuilder {
    private static ICredential credential;

    private static HttpConfig httpConfig;

    private static List<String> KmsEndpoints;

    /**
     * 读取配置文件，初始化参数
     */
    public static void init() {
        //配置认证信息
        ConfigUtils configUtils = ConfigUtils.loadConfig();
        credential = new BasicCredentials()
                .withAk(configUtils.getStringPropertyWithDefault(Constants.CREDENTIAL_AK, null))
                .withSk(configUtils.getStringPropertyWithDefault(Constants.CREDENTIAL_SK, null))
                .withProjectId(configUtils.getStringPropertyWithDefault(Constants.CREDENTIAL_PROJECT_ID, null))
                .withIamEndpoint(configUtils.getStringPropertyWithDefault(Constants.CREDENTIAL_IAM_ENDPOINT, null));
        //http使用默认配置
        httpConfig = HttpConfig.getDefaultHttpConfig();
        //网络代理
        httpConfig.withProxyHost(configUtils.getStringPropertyWithDefault(Constants.PROXY_HOST, null))
                .withProxyPort(configUtils.getIntPropertyWithDefault(Constants.PROXY_PORT, 8080))
                .withProxyUsername(configUtils.getStringPropertyWithDefault(Constants.PROXY_USER, null))
                .withProxyPassword(configUtils.getStringPropertyWithDefault(Constants.PROXY_PASSWORD, null))
                .withTimeout(configUtils.getIntPropertyWithDefault(Constants.TIMEOUT, 10))
                .withIgnoreSSLVerification(configUtils.getStringPropertyWithDefault(Constants.IGNORE_SSL, "fasle").equals("true"));
        //配置是否忽略SSL证书校验
        httpConfig.withIgnoreSSLVerification(true);
        //设置超时时间
        httpConfig.withTimeout(10);
        //客户端初始化
        String[] kmsEndpointList = configUtils.getStringPropertyWithDefault(Constants.KMS_ENDPOINT, null).split(",");
        KmsEndpoints = Arrays.asList(kmsEndpointList);
    }

    /**
     * 构建凭据管理客户端
     *
     * @return CsmsClient
     */
    public static CsmsClient build() {
        //配置认证信息
        init();
        CsmsClient csmsClient = CsmsClient.newBuilder()
                .withCredential(credential)
                .withHttpConfig(httpConfig)
                .withEndpoints(KmsEndpoints)
                .build();
        return csmsClient;
    }
}
