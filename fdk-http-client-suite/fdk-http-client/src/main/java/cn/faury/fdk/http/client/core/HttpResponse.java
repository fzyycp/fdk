/*##############################################################################
 # 基础类库：fdk-http-client
 #
 # @date 5/2018
 # @author faury
 #############################################################################*/

/**
 * 基础类库：ssk-http-client
 *
 * @date 2015年8月14日
 * @author yc.fan
 * <p>
 * 版权所有：南京扫扫看数字科技有限公司
 */
package cn.faury.fdk.http.client.core;

import cn.faury.fdk.common.utils.JsonUtil;
import cn.faury.fdk.common.utils.StringUtil;
import org.apache.http.Header;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;


/**
 * HTTP响应
 */
public class HttpResponse implements Serializable {

    /**
     * 序列化
     */
    private static final long serialVersionUID = 6357019136384078784L;

    /**
     * 是否成功
     */
    public boolean success = false;

    /**
     * 最后一个请求状态码
     */
    public String lastStatusCode = null;

    /**
     * 最后一个请求时间
     */
    public Calendar lastRequestTime = null;

    /**
     * 重试次数
     */
    public int retryCnt = -1;

    /**
     * String类型的result
     */
    private String stringResult;

    /**
     * btye类型的result
     */
    private byte[] byteResult;

    /**
     * 内容类型
     */
    private String contentType = null;

    /**
     * 返回的信息头
     */
    public Header[] headers = null;

    /**
     * 获取byte数组型返回结果
     *
     * @return byte结果
     */
    public byte[] getByteResult() {
        if (byteResult != null) {
            return byteResult;
        }
        if (stringResult != null) {
            return stringResult.getBytes(StringUtil.UTF_8);
        }
        return null;
    }

    /**
     * 获取内容类型
     *
     * @return 内容类型
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * 转换结果字符串到对象
     *
     * @param resultClass
     *            结果对象
     * @return 对象
     */
    public <T> T getResult(Class<T> resultClass) {
        if (null == stringResult) {
            return null;
        }
        return JsonUtil.jsonToObject(stringResult, resultClass);
    }

    /**
     * 获取字符串结果
     *
     * @return 字符串形式结果
     * @throws UnsupportedEncodingException
     */
    public String getStringResult() throws UnsupportedEncodingException {
        if (stringResult != null) {
            return stringResult;
        }
        if (byteResult != null) {
            return new String(byteResult, StringUtil.UTF_8);
        }
        return null;
    }

    /**
     * 设置byte型结果
     *
     * @param byteResult
     *            byte型结果
     */
    public void setByteResult(byte[] byteResult) {
        this.byteResult = byteResult;
    }

    /**
     * 设置类容类型
     *
     * @param contentType
     *            内容类型
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * 设置字符串结果
     *
     * @param stringResult
     *            字符串结果
     */
    public void setStringResult(String stringResult) {
        this.stringResult = stringResult;
    }

    @Override
    public String toString() {
        try {
            return String.format("{success=%s,lastStatusCode=%s,stringResult=%s}", success, lastStatusCode,
                    this.getStringResult());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return super.toString();
        }
    }
}
