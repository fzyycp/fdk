/*##############################################################################
 # 基础类库：fdk-http-client
 #
 # @date 5/2018
 # @author faury
 #############################################################################*/
package cn.faury.fdk.http.client.conf;

import cn.faury.fdk.common.anotation.Properties;
import cn.faury.fdk.common.utils.BeanUtil;
import cn.faury.fdk.common.utils.JsonUtil;
import cn.faury.fdk.common.utils.PropertiesUtil;
import cn.faury.fdk.common.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * http配置信息
 */
public final class HttpConfig implements Serializable {

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
     * 重试等待(0.3秒)
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

    public HttpConfig(String encoding, int socketTimeout, int connectTimeout, int connectRetry, int retryWait
            , boolean redirectsEnabled, String proxyHost, int proxyPort, String proxyProtocol
            , String proxyUsername, String proxyPassword, boolean proxyEnable, boolean sslClientCertification
            , String sslKeystorePath, String sslKeystorePassword, String sslKeystoreFormat) {
        this.encoding = encoding;
        this.socketTimeout = socketTimeout;
        this.connectTimeout = connectTimeout;
        this.connectRetry = connectRetry;
        this.retryWait = retryWait;
        this.redirectsEnabled = redirectsEnabled;
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
        this.proxyProtocol = proxyProtocol;
        this.proxyUsername = proxyUsername;
        this.proxyPassword = proxyPassword;
        this.proxyEnable = proxyEnable;
        this.sslClientCertification = sslClientCertification;
        this.sslKeystorePath = sslKeystorePath;
        this.sslKeystorePassword = sslKeystorePassword;
        this.sslKeystoreFormat = sslKeystoreFormat;
    }

    public String getEncoding() {
        return encoding;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public int getConnectRetry() {
        return connectRetry;
    }

    public int getRetryWait() {
        return retryWait;
    }

    public boolean isRedirectsEnabled() {
        return redirectsEnabled;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public String getProxyProtocol() {
        return proxyProtocol;
    }

    public String getProxyUsername() {
        return proxyUsername;
    }

    public String getProxyPassword() {
        return proxyPassword;
    }

    public boolean isProxyEnable() {
        return proxyEnable;
    }

    public boolean isSslClientCertification() {
        return sslClientCertification;
    }

    public String getSslKeystorePath() {
        return sslKeystorePath;
    }

    public String getSslKeystorePassword() {
        return sslKeystorePassword;
    }

    public String getSslKeystoreFormat() {
        return sslKeystoreFormat;
    }

    @Override
    public String toString() {
        return JsonUtil.objectToJson(this);
    }

    /**
     * 构造器
     */
    public static class Builder {

        // 日志记录器
        private Logger logger = LoggerFactory.getLogger(Builder.class);

        /**
         * 编码
         */
        @Properties(key = "fdk.http.client.encoding", value = StringUtil.UTF8_NAME)
        private String encoding = StringUtil.UTF8_NAME;

        /**
         * Socket超时(8秒)
         */
        @Properties(key = "fdk.http.client.socket.timeout", value = "8000")
        private int socketTimeout = 8000;

        /**
         * 连接超时(8秒)
         */
        @Properties(key = "fdk.http.client.connect.timeout", value = "8000")
        private int connectTimeout = 8000;

        /**
         * 最大重试次数
         */
        @Properties(key = "fdk.http.client.connect.retry", value = "3")
        private int connectRetry = 3;

        /**
         * 重试等待(1秒)
         */
        @Properties(key = "fdk.http.client.connect.retry.wait", value = "300")
        private int retryWait = 300;

        /**
         * 允许重定向
         */
        @Properties(key = "fdk.http.client.redirects.enabled", value = "true")
        private boolean redirectsEnabled = true;

        /**
         * 代理主机
         */
        @Properties(key = "fdk.http.client.proxy.host", value = "")
        private String proxyHost = "";

        /**
         * 代理端口
         */
        @Properties(key = "fdk.http.client.proxy.port", value = "-1")
        private int proxyPort = -1;

        /**
         * 代理协议
         */
        @Properties(key = "fdk.http.client.proxy.protocol", value = "http")
        private String proxyProtocol = "http";

        /**
         * 代理验证用户名
         */
        @Properties(key = "fdk.http.client.proxy.username", value = "")
        private String proxyUsername = "";

        /**
         * 代理验证密码
         */
        @Properties(key = "fdk.http.client.proxy.password", value = "")
        private String proxyPassword = "";

        /**
         * 是否启用代理
         */
        @Properties(key = "fdk.http.client.proxy.enable", value = "false")
        private boolean proxyEnable = false;

        /**
         * 是否使用自定义证书
         */
        @Properties(key = "fdk.http.client.ssl.client.certification", value = "false")
        private boolean sslClientCertification = false;

        /**
         * Client证书位置
         */
        @Properties(key = "fdk.http.client.ssl.keystore.path", value = "")
        private String sslKeystorePath = "";

        /**
         * Client证书密码
         */
        @Properties(key = "fdk.http.client.ssl.keystore.password", value = "")
        private String sslKeystorePassword = "";

        /**
         * Client证书格式
         */
        @Properties(key = "fdk.http.client.ssl.keystore.format", value = "")
        private String sslKeystoreFormat = "";

        /**
         * 构造配置对象
         *
         * @return
         */
        public HttpConfig build() {
            return new HttpConfig(encoding, socketTimeout, connectTimeout, connectRetry, retryWait
                    , redirectsEnabled, proxyHost, proxyPort, proxyProtocol
                    , proxyUsername, proxyPassword, proxyEnable, sslClientCertification, sslKeystorePath
                    , sslKeystorePassword, sslKeystoreFormat);
        }

        /**
         * 从配置文件中进行构造
         *
         * @param properties 配置文件路径
         * @return 配置对象
         */
        public HttpConfig buildFromFile(String properties) {
            // 初始化配置文件
            PropertiesUtil cfg;
            try {
                if (logger.isTraceEnabled()) {
                    logger.trace("》读取配置文件 Begin=====");
                }
                cfg = PropertiesUtil.createPropertyInstance(properties);
                if (cfg != null) {
                    BeanUtil.initFields(this, cfg);
                    if (logger.isTraceEnabled()) {
                        logger.trace("》读取配置文件 Success=====");
                    }
                }
            } catch (Exception e) {
                if (logger.isTraceEnabled()) {
                    logger.trace("配置文件不存在或格式错误，采用默认配置", e);
                }
            }


            return build();
        }

        public Builder setProxyHost(String proxyHost) {
            this.proxyHost = proxyHost;
            return this;
        }

        public Builder setEncoding(String encoding) {
            this.encoding = encoding;
            return this;
        }

        public Builder setSocketTimeout(int socketTimeout) {
            this.socketTimeout = socketTimeout;
            return this;
        }

        public Builder setConnectTimeout(int connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }

        public Builder setConnectRetry(int connectRetry) {
            this.connectRetry = connectRetry;
            return this;
        }

        public Builder setRetryWait(int retryWait) {
            this.retryWait = retryWait;
            return this;
        }

        public Builder setRedirectsEnabled(boolean redirectsEnabled) {
            this.redirectsEnabled = redirectsEnabled;
            return this;
        }

        public Builder setProxyPort(int proxyPort) {
            this.proxyPort = proxyPort;
            return this;
        }

        public Builder setProxyProtocol(String proxyProtocol) {
            this.proxyProtocol = proxyProtocol;
            return this;
        }

        public Builder setProxyUsername(String proxyUsername) {
            this.proxyUsername = proxyUsername;
            return this;
        }

        public Builder setProxyPassword(String proxyPassword) {
            this.proxyPassword = proxyPassword;
            return this;
        }

        public Builder setProxyEnable(boolean proxyEnable) {
            this.proxyEnable = proxyEnable;
            return this;
        }

        public Builder setSslClientCertification(boolean sslClientCertification) {
            this.sslClientCertification = sslClientCertification;
            return this;
        }

        public Builder setSslKeystorePath(String sslKeystorePath) {
            this.sslKeystorePath = sslKeystorePath;
            return this;
        }

        public Builder setSslKeystorePassword(String sslKeystorePassword) {
            this.sslKeystorePassword = sslKeystorePassword;
            return this;
        }

        public Builder setSslKeystoreFormat(String sslKeystoreFormat) {
            this.sslKeystoreFormat = sslKeystoreFormat;
            return this;
        }
    }

}
