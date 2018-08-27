package cn.faury.fdk.pay.autoconfigure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 微信支付配置文件
 */
@ConfigurationProperties(prefix = FdkPayTencentProperties.PROPERTIES_PREFIX)
public class FdkPayTencentProperties {

    /**
     * 配置文件前缀
     */
    public static final String PROPERTIES_PREFIX = "fdk.pay.tenpay";

    /**
     * APP_ID：微信分配的ID
     */
    private String appId;

    /**
     * 微信支付分配的商户号ID
     */
    private String partner;

    /**
     * PARTNER_KEY
     */
    private String key;

    /**
     * HTTPS证书的本地路径
     */
    private String certLocalPath;

    /**
     * HTTPS证书密码，默认密码等于商户号MCHID
     */
    private String certPassword;

    /**
     * 字符编码格式 目前支持 gbk 或 utf-8
     */
    private String inputCharset = "utf-8";;

    /**
     * 回调地址
     */
    private String notifyUrl = "";;

    /**
     * 获取appId
     *
     * @return appId
     */
    public String getAppId() {
        return appId;
    }

    /**
     * 设置appId
     *
     * @param appId 值
     */
    public void setAppId(String appId) {
        this.appId = appId;
    }

    /**
     * 获取partner
     *
     * @return partner
     */
    public String getPartner() {
        return partner;
    }

    /**
     * 设置partner
     *
     * @param partner 值
     */
    public void setPartner(String partner) {
        this.partner = partner;
    }

    /**
     * 获取key
     *
     * @return key
     */
    public String getKey() {
        return key;
    }

    /**
     * 设置key
     *
     * @param key 值
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * 获取certLocalPath
     *
     * @return certLocalPath
     */
    public String getCertLocalPath() {
        return certLocalPath;
    }

    /**
     * 设置certLocalPath
     *
     * @param certLocalPath 值
     */
    public void setCertLocalPath(String certLocalPath) {
        this.certLocalPath = certLocalPath;
    }

    /**
     * 获取certPassword
     *
     * @return certPassword
     */
    public String getCertPassword() {
        return certPassword;
    }

    /**
     * 设置certPassword
     *
     * @param certPassword 值
     */
    public void setCertPassword(String certPassword) {
        this.certPassword = certPassword;
    }

    /**
     * 获取inputCharset
     *
     * @return inputCharset
     */
    public String getInputCharset() {
        return inputCharset;
    }

    /**
     * 设置inputCharset
     *
     * @param inputCharset 值
     */
    public void setInputCharset(String inputCharset) {
        this.inputCharset = inputCharset;
    }

    /**
     * 获取notifyUrl
     *
     * @return notifyUrl
     */
    public String getNotifyUrl() {
        return notifyUrl;
    }

    /**
     * 设置notifyUrl
     *
     * @param notifyUrl 值
     */
    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }
}
