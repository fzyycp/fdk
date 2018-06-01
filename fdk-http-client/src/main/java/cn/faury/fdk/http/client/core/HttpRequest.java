/*##############################################################################
 # 基础类库：fdk-http-client
 #
 # @date 6/2018
 # @author faury
 #############################################################################*/
package cn.faury.fdk.http.client.core;

import cn.faury.fdk.common.utils.JsonUtil;
import cn.faury.fdk.http.client.conf.HttpConfig;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 请求实体
 */
public class HttpRequest implements Serializable {

    /**
     * 待请求的url
     */
    public String uri = null;

    /**
     * 默认的请求方式
     */
    public String method = HttpGet.METHOD_NAME;

    /**
     * 请求参数值对
     */
    public List<NameValuePair> parameters = new ArrayList<>();
    /**
     * 请求头
     */
    public List<Header> addHeaders = new ArrayList<>();

    /**
     * cookie
     */
    public String cookie = null;

    /**
     * 请求返回的格式
     */
    public HttpResultType resultType = HttpResultType.BYTES;

    /**
     * 使用返回体
     */
    public boolean useResponseBody = true;

    /**
     * 最后一次请求
     */
    public HttpRequest lastRequest = null;

    /**
     * 请求配置参数
     */
    public HttpConfig httpConfig = null;

    private HttpRequest(){}

    public String getUri() {
        return uri;
    }

    public HttpRequest setUri(String uri) {
        this.uri = uri;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public HttpRequest setMethod(String method) {
        this.method = method;
        return this;
    }

    public List<NameValuePair> getParameters() {
        return parameters;
    }

    public HttpRequest setParameters(List<NameValuePair> parameters) {
        this.parameters = parameters;
        return this;
    }

    public List<Header> getAddHeaders() {
        return addHeaders;
    }

    public HttpRequest setAddHeaders(List<Header> addHeaders) {
        this.addHeaders = addHeaders;
        return this;
    }

    public String getCookie() {
        return cookie;
    }

    public HttpRequest setCookie(String cookie) {
        this.cookie = cookie;
        return this;
    }

    public HttpResultType getResultType() {
        return resultType;
    }

    public HttpRequest setResultType(HttpResultType resultType) {
        this.resultType = resultType;
        return this;
    }

    public boolean isUseResponseBody() {
        return useResponseBody;
    }

    public HttpRequest setUseResponseBody(boolean useResponseBody) {
        this.useResponseBody = useResponseBody;
        return this;
    }

    public HttpRequest getLastRequest() {
        return lastRequest;
    }

    public HttpRequest setLastRequest(HttpRequest lastRequest) {
        this.lastRequest = lastRequest;
        return this;
    }

    public HttpConfig getHttpConfig() {
        return httpConfig;
    }

    public HttpRequest setHttpConfig(HttpConfig httpConfig) {
        this.httpConfig = httpConfig;
        return this;
    }

    @Override
    public String toString() {
        return JsonUtil.objectToJson(this);
    }

    /**
     * 构造器
     */
    public static class Builder{
        /**
         * 创建字符串型请求对象
         * @return 请求对象
         */
        public static HttpRequest createStringRequest(){
            return new HttpRequest().setResultType(HttpResultType.STRING);
        }
        /**
         * 创建字节型请求对象
         * @return 请求对象
         */
        public static HttpRequest createBytesRequest(){
            return new HttpRequest().setResultType(HttpResultType.BYTES);
        }
    }
}
