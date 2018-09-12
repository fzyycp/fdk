package cn.faury.fdk.pay.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 微信支付配置文件
 */
@ConfigurationProperties(prefix = FdkPayAlipayProperties.PROPERTIES_PREFIX)
public class FdkPayAlipayProperties {

    /**
     * 配置文件前缀
     */
    public static final String PROPERTIES_PREFIX = "fdk.pay.alipay";

    /**
     * APP_ID：支付宝分配的ID
     */
    private String appId;

    /**
     * 支付宝分配的商户号ID
     */
    private String partner;

    /**
     * PARTNER_KEY
     */
    private String privateKey;

    /**
     * PARTNER_KEY
     */
    private String publicKey;

    /**
     * 字符编码格式 目前支持 gbk 或 utf-8
     */
    private String inputCharset = "utf-8";;

    /**
     * 签名方式
     */
    private String signType = "RSA";;

    /**
     * 回调地址
     */
    private String notifyUrl = "";;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getInputCharset() {
        return inputCharset;
    }

    public void setInputCharset(String inputCharset) {
        this.inputCharset = inputCharset;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }
}
