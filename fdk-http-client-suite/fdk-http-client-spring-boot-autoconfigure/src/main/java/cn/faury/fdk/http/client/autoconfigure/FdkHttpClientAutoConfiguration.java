package cn.faury.fdk.http.client.autoconfigure;

import cn.faury.fdk.http.client.HttpClient;
import cn.faury.fdk.http.client.HttpUtil;
import cn.faury.fdk.http.client.conf.HttpConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(FdkHttpClientProperties.class)
public class FdkHttpClientAutoConfiguration {

    @Autowired(required = false)
    private FdkHttpClientProperties fdkHttpClientProperties;

    @Bean
    public HttpConfig httpConfig() {
        return new HttpConfig(fdkHttpClientProperties.getEncoding(), fdkHttpClientProperties.getSocketTimeout()
                , fdkHttpClientProperties.getConnectTimeout(), fdkHttpClientProperties.getConnectRetry()
                , fdkHttpClientProperties.getRetryWait(), fdkHttpClientProperties.isRedirectsEnabled()
                , fdkHttpClientProperties.getProxyHost(), fdkHttpClientProperties.getProxyPort(), fdkHttpClientProperties.getProxyProtocol()
                , fdkHttpClientProperties.getProxyUsername(), fdkHttpClientProperties.getProxyPassword(), fdkHttpClientProperties.isProxyEnable()
                , fdkHttpClientProperties.isSslClientCertification(), fdkHttpClientProperties.getSslKeystorePath()
                , fdkHttpClientProperties.getSslKeystorePassword(), fdkHttpClientProperties.getSslKeystoreFormat());
    }

    @Bean
    public HttpClient httpClient(HttpConfig httpConfig) {
        HttpClient httpClient = new HttpClient(httpConfig);
        HttpUtil.init(httpClient);
        return httpClient;
    }
}
