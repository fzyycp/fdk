package cn.faury.fdk.http.client.autoconfigure;

import cn.faury.fdk.common.utils.StringUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 配置文件
 */
@ConfigurationProperties(prefix = FdkHttpClientProperties.PROPERTIES_PREFIX)
public class FdkHttpClientProperties {

    /**
     * 配置文件前缀
     */
    public static final String PROPERTIES_PREFIX = "fdk.http.client";

    /**
     * 编码
     */
    private String encoding = StringUtil.UTF8_NAME;

    /**
     * Socket超时(8秒)
     */
    private int socketTimeout = 8000;

    /**
     * 连接超时(8秒)
     */
    private int connectTimeout = 8000;

    /**
     * 最大重试次数
     */
    private int connectRetry = 3;

    /**
     * 重试等待(1秒)
     */
    private int retryWait = 300;

    /**
     * 允许重定向
     */
    private boolean redirectsEnabled = true;

    /**
     * 代理主机
     */
    private String proxyHost = "";

    /**
     * 代理端口
     */
    private int proxyPort = -1;

    /**
     * 代理协议
     */
    private String proxyProtocol = "http";

    /**
     * 代理验证用户名
     */
    private String proxyUsername = "";

    /**
     * 代理验证密码
     */
    private String proxyPassword = "";

    /**
     * 是否启用代理
     */
    private boolean proxyEnable = false;

    /**
     * 是否使用自定义证书
     */
    private boolean sslClientCertification = false;

    /**
     * Client证书位置
     */
    private String sslKeystorePath = "";

    /**
     * Client证书密码
     */
    private String sslKeystorePassword = "";

    /**
     * Client证书格式
     */
    private String sslKeystoreFormat = "";

    /**
     * 获取encoding
     *
     * @return encoding
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * 设置encoding
     *
     * @param encoding 值
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
     * 获取socketTimeout
     *
     * @return socketTimeout
     */
    public int getSocketTimeout() {
        return socketTimeout;
    }

    /**
     * 设置socketTimeout
     *
     * @param socketTimeout 值
     */
    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    /**
     * 获取connectTimeout
     *
     * @return connectTimeout
     */
    public int getConnectTimeout() {
        return connectTimeout;
    }

    /**
     * 设置connectTimeout
     *
     * @param connectTimeout 值
     */
    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    /**
     * 获取connectRetry
     *
     * @return connectRetry
     */
    public int getConnectRetry() {
        return connectRetry;
    }

    /**
     * 设置connectRetry
     *
     * @param connectRetry 值
     */
    public void setConnectRetry(int connectRetry) {
        this.connectRetry = connectRetry;
    }

    /**
     * 获取retryWait
     *
     * @return retryWait
     */
    public int getRetryWait() {
        return retryWait;
    }

    /**
     * 设置retryWait
     *
     * @param retryWait 值
     */
    public void setRetryWait(int retryWait) {
        this.retryWait = retryWait;
    }

    /**
     * 获取redirectsEnabled
     *
     * @return redirectsEnabled
     */
    public boolean isRedirectsEnabled() {
        return redirectsEnabled;
    }

    /**
     * 设置redirectsEnabled
     *
     * @param redirectsEnabled 值
     */
    public void setRedirectsEnabled(boolean redirectsEnabled) {
        this.redirectsEnabled = redirectsEnabled;
    }

    /**
     * 获取proxyHost
     *
     * @return proxyHost
     */
    public String getProxyHost() {
        return proxyHost;
    }

    /**
     * 设置proxyHost
     *
     * @param proxyHost 值
     */
    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    /**
     * 获取proxyPort
     *
     * @return proxyPort
     */
    public int getProxyPort() {
        return proxyPort;
    }

    /**
     * 设置proxyPort
     *
     * @param proxyPort 值
     */
    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    /**
     * 获取proxyProtocol
     *
     * @return proxyProtocol
     */
    public String getProxyProtocol() {
        return proxyProtocol;
    }

    /**
     * 设置proxyProtocol
     *
     * @param proxyProtocol 值
     */
    public void setProxyProtocol(String proxyProtocol) {
        this.proxyProtocol = proxyProtocol;
    }

    /**
     * 获取proxyUsername
     *
     * @return proxyUsername
     */
    public String getProxyUsername() {
        return proxyUsername;
    }

    /**
     * 设置proxyUsername
     *
     * @param proxyUsername 值
     */
    public void setProxyUsername(String proxyUsername) {
        this.proxyUsername = proxyUsername;
    }

    /**
     * 获取proxyPassword
     *
     * @return proxyPassword
     */
    public String getProxyPassword() {
        return proxyPassword;
    }

    /**
     * 设置proxyPassword
     *
     * @param proxyPassword 值
     */
    public void setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
    }

    /**
     * 获取proxyEnable
     *
     * @return proxyEnable
     */
    public boolean isProxyEnable() {
        return proxyEnable;
    }

    /**
     * 设置proxyEnable
     *
     * @param proxyEnable 值
     */
    public void setProxyEnable(boolean proxyEnable) {
        this.proxyEnable = proxyEnable;
    }

    /**
     * 获取sslClientCertification
     *
     * @return sslClientCertification
     */
    public boolean isSslClientCertification() {
        return sslClientCertification;
    }

    /**
     * 设置sslClientCertification
     *
     * @param sslClientCertification 值
     */
    public void setSslClientCertification(boolean sslClientCertification) {
        this.sslClientCertification = sslClientCertification;
    }

    /**
     * 获取sslKeystorePath
     *
     * @return sslKeystorePath
     */
    public String getSslKeystorePath() {
        return sslKeystorePath;
    }

    /**
     * 设置sslKeystorePath
     *
     * @param sslKeystorePath 值
     */
    public void setSslKeystorePath(String sslKeystorePath) {
        this.sslKeystorePath = sslKeystorePath;
    }

    /**
     * 获取sslKeystorePassword
     *
     * @return sslKeystorePassword
     */
    public String getSslKeystorePassword() {
        return sslKeystorePassword;
    }

    /**
     * 设置sslKeystorePassword
     *
     * @param sslKeystorePassword 值
     */
    public void setSslKeystorePassword(String sslKeystorePassword) {
        this.sslKeystorePassword = sslKeystorePassword;
    }

    /**
     * 获取sslKeystoreFormat
     *
     * @return sslKeystoreFormat
     */
    public String getSslKeystoreFormat() {
        return sslKeystoreFormat;
    }

    /**
     * 设置sslKeystoreFormat
     *
     * @param sslKeystoreFormat 值
     */
    public void setSslKeystoreFormat(String sslKeystoreFormat) {
        this.sslKeystoreFormat = sslKeystoreFormat;
    }
}
