/*##############################################################################
 # 基础类库：fdk-http-client
 #
 # @date 5/2018
 # @author faury
 #############################################################################*/
package cn.faury.fdk.http.client;

import cn.faury.fdk.common.anotation.NonNull;
import cn.faury.fdk.common.utils.AssertUtil;
import cn.faury.fdk.common.utils.DateUtil;
import cn.faury.fdk.common.utils.StringUtil;
import cn.faury.fdk.http.client.conf.HttpConfig;
import cn.faury.fdk.http.client.core.HttpRequest;
import cn.faury.fdk.http.client.core.HttpResponse;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.cookie.SM;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Collections;
import java.util.List;

/**
 * Http请求客户端
 */
public class HttpClient {
    // 日志输出
    private static final Logger logger = LoggerFactory.getLogger(HttpClient.class);

    /**
     * 状态码：错误
     */
    public static final String STATUS_CODE_ERROR = "ERROR";

    /**
     * 状态码：Socket超时
     */
    public static final String STATUS_CODE_ERROR_SOTIMEOUT = "ERROR_SOTIMEOUT";

    /**
     * 状态码：连接超时
     */
    public static final String STATUS_CODE_ERROR_COTIMEOUT = "ERROR_COTIMEOUT";

    /**
     * 状态码：服务器连接错误
     */
    public static final String STATUS_CODE_ERROR_HOSTCONNECT = "ERROR_HOSTCONNECT";

    /**
     * 状态码：URI错误
     */
    public static final String STATUS_CODE_ERROR_URISYNTAX = "ERROR_URISYNTAX";

    /**
     * 全局最大连接数
     */
    public static final int defaultMaxTotalConn = 200;

    /**
     * 每个客户端最大连接数
     */
    public static final int defaultMaxConnPerHost = 20;

    /**
     * Http连接池
     */
    private PoolingHttpClientConnectionManager cm;

    /**
     * http请求客户端
     */
    private CloseableHttpClient httpClient;

    /**
     * cookie对象
     */
    private CookieStore cookieStore;

    // 默认的配置
    private HttpConfig defaultConfig;

    // 构造函数
    HttpClient(HttpConfig defaultConfig) {
        AssertUtil.assertNotNull(defaultConfig, "HttpClient config cannot be null");
        // 创建连接池
        cm = new PoolingHttpClientConnectionManager();
        // 数值连接池全局最大连接数
        cm.setMaxTotal(defaultMaxTotalConn);
        // 设置每个客户端最大连接数
        cm.setDefaultMaxPerRoute(defaultMaxConnPerHost);
        // 设置cookie存储器
        cookieStore = new BasicCookieStore();
        // 连接配置
        this.defaultConfig = defaultConfig;
    }

    /**
     * 发送请求
     *
     * @param httpRequest 请求实体
     * @return 返回值
     */
    public HttpResponse doRequest(@NonNull HttpRequest httpRequest) {
        logger.trace("{}", "=====准备通过HttpClient执行Http请求=====");
        // 输入验证
        if (httpRequest == null) {
            logger.trace("{}", "》请求URL为空！");
            return null;
        } else {
            logger.trace("{}", "》请求URL：" + httpRequest.uri);
        }
        HttpResponse r = new HttpResponse();
        createHttpClient(httpRequest);

        HttpConfig config = httpRequest.httpConfig == null ? defaultConfig : httpRequest.httpConfig;
        logger.trace("{}", "》HttpUtil使用代理设置为：" + (config != null ? config.toString() : "null"));
        r.lastStatusCode = STATUS_CODE_ERROR;
        HttpRequestBase request = null;
        do {
            ++r.retryCnt;
            if (r.retryCnt > 0 && config.getRetryWait() > 0) {
                logger.trace("{}", "连接失败，第" + r.retryCnt + "次重试");
                retryWait(config.getRetryWait());
            }

            try {
                r.lastRequestTime = DateUtil.getCurrentCalendar();

                if (HttpGet.METHOD_NAME.equalsIgnoreCase(httpRequest.method)) {
                    try {
                        URIBuilder uriBuilder = new URIBuilder(httpRequest.uri);
                        if (httpRequest.parameters != null && !httpRequest.parameters.isEmpty()) {
                            uriBuilder.setParameters(httpRequest.parameters);
                        }
                        URI uri = uriBuilder.build();
                        request = new HttpGet(uri);
                    } catch (URISyntaxException e) {
                        r.lastStatusCode = STATUS_CODE_ERROR_URISYNTAX;
                        break;
                    }

                } else {
                    HttpPost requestPost = new HttpPost(httpRequest.uri);
                    requestPost.setEntity(new UrlEncodedFormEntity(httpRequest.parameters, config.getEncoding()));
                    request = requestPost;
                }
                addHeader(request, httpRequest.addHeaders);
                if (StringUtil.isNotEmpty(httpRequest.cookie)) {
                    Header cookie = new BasicHeader(SM.COOKIE, httpRequest.cookie);
                    addHeader(request, Collections.singletonList(cookie));
                }
                CloseableHttpResponse lastHttpResponse = httpClient.execute(request);
                int statusCode = lastHttpResponse.getStatusLine().getStatusCode();
                r.lastStatusCode = String.valueOf(statusCode);
                r.success = (statusCode == HttpStatus.SC_OK);
                r.headers = lastHttpResponse.getAllHeaders();
                HttpEntity entity = lastHttpResponse.getEntity();
                r.setContentType(getLenientOrDefault(entity).getMimeType());
                if (httpRequest.useResponseBody) {
                    switch (httpRequest.resultType) {
                        case STRING:
                            r.setStringResult(EntityUtils.toString(entity, config.getEncoding()));
                            break;
                        case BYTES:
                            r.setByteResult(EntityUtils.toByteArray(entity));
                            break;
                    }
                }
                break;
            } catch (SocketTimeoutException e) {
                // retry
                r.lastStatusCode = STATUS_CODE_ERROR_SOTIMEOUT;
            } catch (ConnectTimeoutException e) {
                r.lastStatusCode = STATUS_CODE_ERROR_COTIMEOUT;
                break;
            } catch (HttpHostConnectException e) {
                // retry
                r.lastStatusCode = STATUS_CODE_ERROR_HOSTCONNECT;
            } catch (Exception e) {
                r.lastStatusCode = e.getClass().getName();
                break;
            }
        } while (r.retryCnt < config.getConnectRetry());

        if (request != null) {
            request.releaseConnection();
        }

        return r;

    }

    private void createHttpClient(@NonNull HttpRequest httpRequest) {
        HttpConfig config = httpRequest.httpConfig == null ? defaultConfig : httpRequest.httpConfig;
        HttpClientBuilder hcb = HttpClients.custom().setConnectionManager(cm)
                .setDefaultCookieStore(cookieStore);
        if (StringUtil.isNotEmpty(httpRequest.uri) && httpRequest.uri.startsWith("https") && config.isSslClientCertification()) {
            SSLConnectionSocketFactory sslConSF = createSSLSocketFactory(config);
            hcb.setSSLSocketFactory(sslConSF);
            // 无密码代理
            if (config.isProxyEnable() && StringUtil.isNotEmpty(config.getProxyHost())) {
                HttpHost proxy = new HttpHost(config.getProxyHost(), config.getProxyPort(), config.getProxyProtocol());
                hcb.setProxy(proxy);
            }
        } else {
            RequestConfig.Builder builder = RequestConfig.custom()
                    .setConnectTimeout(config.getConnectTimeout())
                    .setSocketTimeout(config.getSocketTimeout())
                    .setRedirectsEnabled(config.isRedirectsEnabled()).setExpectContinueEnabled(false);
            // 无密码代理
            if (config.isProxyEnable()) {
                AssertUtil.assertTrue(StringUtil.isNotEmpty(config.getProxyHost()), "代理配置错误，host不可以为空");
                AssertUtil.assertTrue((config.getProxyPort() > 0 && config.getProxyPort() < 65536), String.format("代理配置错误，port不可以为%s，必须为[0~65535]", config.getProxyPort()));
                HttpHost proxy = new HttpHost(config.getProxyHost(), config.getProxyPort(), config.getProxyProtocol());
                builder.setProxy(proxy);
            }
            hcb.setDefaultRequestConfig(builder.build());
        }
        httpClient = hcb.build();
    }

    /**
     * 添加请求头
     *
     * @param request    请求
     * @param addHeaders 请求头
     */
    private static void addHeader(HttpRequestBase request, List<Header> addHeaders) {
        if (request != null && addHeaders != null) {
            for (Header header : addHeaders) {
                request.addHeader(header);
            }
        }
    }

    /**
     * 重试等待
     *
     * @param wait 等待时间（毫秒）
     */
    private static void retryWait(int wait) {
        try {
            Thread.sleep(wait);
        } catch (InterruptedException e) {

        }
    }

    /**
     * 创建SSL安全套接字
     *
     * @param config http配置
     * @return SSL连接工厂
     */
    private static SSLConnectionSocketFactory createSSLSocketFactory(HttpConfig config) {
        try {
            if (config.isSslClientCertification()) {
                KeyStore store = KeyStore.getInstance(StringUtil.emptyDefault(config.getSslKeystoreFormat(), KeyStore.getDefaultType()));
                store.load(new FileInputStream(config.getSslKeystorePath()), config.getSslKeystorePassword().toCharArray());
                SSLContext sslsContext = SSLContexts.custom().loadTrustMaterial(store).build();
                return new SSLConnectionSocketFactory(sslsContext,
                        SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
            }
            SSLContextBuilder builder = new SSLContextBuilder();
            builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
            return new SSLConnectionSocketFactory(builder.build());

        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException
                | KeyManagementException e) {
        }
        return null;

    }

    /**
     * 获取ContentType，忽略异常，异常情况下返回默认值
     *
     * @param entity 请求实体
     * @return ContentType
     */
    private static ContentType getLenientOrDefault(final HttpEntity entity) {
        final ContentType contentType = ContentType.getLenient(entity);
        return contentType != null ? contentType : ContentType.DEFAULT_TEXT;
    }
}
