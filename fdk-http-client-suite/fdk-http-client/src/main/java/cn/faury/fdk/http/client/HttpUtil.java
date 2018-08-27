/*##############################################################################
 # 基础类库：fdk-http-client
 #
 # @date 5/2018
 # @author faury
 #############################################################################*/
package cn.faury.fdk.http.client;

import cn.faury.fdk.common.anotation.NonNull;
import cn.faury.fdk.common.anotation.Nullable;
import cn.faury.fdk.common.entry.RestResultCode;
import cn.faury.fdk.common.exception.TipsException;
import cn.faury.fdk.common.utils.StringUtil;
import cn.faury.fdk.http.client.conf.HttpConfig;
import cn.faury.fdk.http.client.core.HttpRequest;
import cn.faury.fdk.http.client.core.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Http请求类(支持代理)
 * <p>
 * <pre>
 * 目前不支持设置用户名、密码
 * </pre>
 */
public class HttpUtil {

    /**
     * 日志记录器
     */
    private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    // 是否初始化
    private static boolean isInit = false;
    // http请求配置参数
    private static HttpConfig httpConfig = null;
    // http请求客户端
    private static HttpClient httpClient = null;

    /**
     * 私有构造函数
     */
    private HttpUtil() {
    }

    /**
     * 初始化代理配置
     *
     * @param properties 配置文件路径
     */
    public static void init(InputStream properties) {
        logger.debug("{}", "=====初始化HttpUtil Start=====");
        if (!isInit) {
            synchronized (HttpUtil.class) {
                if (!isInit) {
                    logger.debug("{}", "》开始读取配置文件[" + properties + "]=====");
                    try {
                        // 初始化配置文件
                        httpConfig = (new HttpConfig.Builder()).buildFromInputStream(properties);
                        logger.debug("{}", "》从配置文件[" + properties + "]装配bean完成=====");
                    } catch (Exception e) {
                        httpConfig = (new HttpConfig.Builder()).build();
                        logger.debug("{}", "配置文件文件不存在或格式错误，采用默认配置", e);
                    }
                    httpClient = new HttpClient(httpConfig);
                    isInit = true;
                } else {
                    logger.debug("{}", "》HttpUtil已经存在无需重复初始化=====");
                }
            }
        } else {
            logger.debug("{}", "》HttpUtil已经存在无需重复初始化=====");
        }
        logger.debug("{}", "》配置参数：" + httpConfig.toString());
        logger.debug("{}", "=====初始化HttpUtil Finish=====");
    }

    /**
     * 初始化代理配置
     *
     * @param client 配置
     */
    public static void init(HttpClient client) {
        logger.debug("{}", "=====初始化HttpUtil Start=====");
        if (!isInit) {
            synchronized (HttpUtil.class) {
                if (!isInit) {
                    logger.debug("{}", "》开始设置配置对象=====");
                    // 初始化配置文件
                    httpClient = client;
                    httpConfig = client.getDefaultConfig();
                    isInit = true;
                } else {
                    logger.debug("{}", "》HttpUtil已经存在无需重复初始化=====");
                }
            }
        } else {
            logger.debug("{}", "》HttpUtil已经存在无需重复初始化=====");
        }
        logger.debug("{}", "》配置参数：" + httpConfig.toString());
        logger.debug("{}", "=====初始化HttpUtil Finish=====");
    }

    /**
     * 执行Http请求
     *
     * @param request Http请求
     * @return 返回响应结果
     */
    public static HttpResponse execute(@NonNull HttpRequest request) {
        checkInit();
        return httpClient.doRequest(request);
    }

    /**
     * 请求URI
     *
     * @param uri         资源地址
     * @param method      请求方式GET、POST、PUT
     * @param parameters  请求参数
     * @param cookie      请求cookie
     * @param lastRequest 上一次请求
     * @return 请求结果
     */
    public static HttpResponse execute(@NonNull String uri, @Nullable String method, @Nullable List<NameValuePair> parameters, @Nullable String cookie, @Nullable HttpRequest lastRequest) {
        HttpRequest httpRequest = HttpRequest.Builder.createStringRequest()
                .setUri(uri)
                .setMethod(StringUtil.emptyDefault(method, HttpGet.METHOD_NAME))
                .setParameters(parameters)
                .setCookie(cookie)
                .setLastRequest(lastRequest);
        return execute(httpRequest);
    }

    /**
     * GET方式请求URI
     *
     * @param uri 资源地址
     * @return 请求结果
     */
    public static HttpResponse get(@NonNull String uri) {
        return get(uri, null);
    }

    /**
     * GET方式请求URI
     *
     * @param uri        资源地址
     * @param parameters 请求参数
     * @return 请求结果
     */
    public static HttpResponse get(@NonNull String uri, @Nullable ArrayList<NameValuePair> parameters) {
        return get(uri, parameters, null);
    }

    /**
     * GET方式请求URI
     *
     * @param uri        资源地址
     * @param parameters 请求参数
     * @param cookie     请求cookie
     * @return 请求结果
     */
    public static HttpResponse get(@NonNull String uri, @Nullable ArrayList<NameValuePair> parameters, @Nullable String cookie) {
        return get(uri, parameters, cookie, null);
    }

    /**
     * GET方式请求URI
     *
     * @param uri        资源地址
     * @param parameters 请求参数
     * @param cookie     请求cookie
     * @return 请求结果
     */
    public static HttpResponse get(@NonNull String uri, @Nullable ArrayList<NameValuePair> parameters, @Nullable String cookie, @Nullable HttpRequest lastRequest) {
        return execute(uri, HttpGet.METHOD_NAME, parameters, cookie, lastRequest);
    }

    /**
     * POST方式请求URI
     *
     * @param uri 资源地址
     * @return 请求结果
     */
    public static HttpResponse post(@NonNull String uri) {
        return post(uri, null);
    }

    /**
     * POST方式请求URI
     *
     * @param uri        资源地址
     * @param parameters 请求参数
     * @return 请求结果
     */
    public static HttpResponse post(@NonNull String uri, @Nullable ArrayList<NameValuePair> parameters) {
        return post(uri, parameters, null);
    }

    /**
     * POST方式请求URI
     *
     * @param uri        资源地址
     * @param parameters 请求参数
     * @param cookie     请求cookie
     * @return 请求结果
     */
    public static HttpResponse post(@NonNull String uri, @Nullable ArrayList<NameValuePair> parameters, @Nullable String cookie) {
        return post(uri, parameters, cookie, null);
    }

    /**
     * POST方式请求URI
     *
     * @param uri        资源地址
     * @param parameters 请求参数
     * @param cookie     请求cookie
     * @return 请求结果
     */
    public static HttpResponse post(@NonNull String uri, @Nullable ArrayList<NameValuePair> parameters, @Nullable String cookie,
                                    @Nullable HttpRequest lastRequest) {
        return execute(uri, HttpPost.METHOD_NAME, parameters, cookie, lastRequest);
    }

    /**
     * 初始化Check
     */
    protected static void checkInit() {
        if (!isInit) {
            logger.debug("{}", "checkInit error[HttpUtil not init]!");
            throw new TipsException(RestResultCode.CODE500.getCode(), "HttpUtil not init");
        }
    }
}
