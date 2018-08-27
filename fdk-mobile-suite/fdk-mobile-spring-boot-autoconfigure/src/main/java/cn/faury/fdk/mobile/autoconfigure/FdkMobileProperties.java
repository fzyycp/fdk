package cn.faury.fdk.mobile.autoconfigure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * 配置文件
 */
@ConfigurationProperties(prefix = FdkMobileProperties.PROPERTIES_PREFIX)
public class FdkMobileProperties {

    /**
     * 配置文件前缀
     */
    public static final String PROPERTIES_PREFIX = "fdk.mobile";
    // 默认Cookie名称
    public static final String DEFAULT_COOKIE_NAME = "S";
    // 默认Session超时时间
    public static final int DEFAULT_SESSION_TIMEOUT = 86400;

    private String appCode;

    private int sessionTimeout = DEFAULT_SESSION_TIMEOUT;

    private String cookieName = DEFAULT_COOKIE_NAME;

    @Value("${" + PROPERTIES_PREFIX + ".oauth.cfgs.weixin:}")
    private String weixinOAuth2Keys;

    @Value("${" + PROPERTIES_PREFIX + ".oauth.cfgs.weixinmp:}")
    private String weixinmpOAuth2Keys;

    @Value("${" + PROPERTIES_PREFIX + ".oauth.cfgs.qq:}")
    private String qqOAuth2Keys;

    @Value("${" + PROPERTIES_PREFIX + ".oauth.cfgs.sinaweibo:}")
    private String sinaweiboOAuth2Keys;

    @Value("${" + PROPERTIES_PREFIX + ".oauth.nounionid.weixinmp:}")
    private String weixinmpNoUnionId;

    @Value("#{'${" + PROPERTIES_PREFIX + ".filter.anon:}'.split(',')}")
    private List<String> anon = new ArrayList<>();

    /**
     * 获取appCode
     *
     * @return appCode
     */
    public String getAppCode() {
        return appCode;
    }

    /**
     * 设置appCode
     *
     * @param appCode 值
     */
    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    /**
     * 获取sessionTimeout
     *
     * @return sessionTimeout
     */
    public int getSessionTimeout() {
        return sessionTimeout;
    }

    /**
     * 设置sessionTimeout
     *
     * @param sessionTimeout 值
     */
    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    /**
     * 获取cookieName
     *
     * @return cookieName
     */
    public String getCookieName() {
        return cookieName;
    }

    /**
     * 设置cookieName
     *
     * @param cookieName 值
     */
    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }

    /**
     * 获取weixinOAuth2Keys
     *
     * @return weixinOAuth2Keys
     */
    public String getWeixinOAuth2Keys() {
        return weixinOAuth2Keys;
    }

    /**
     * 设置weixinOAuth2Keys
     *
     * @param weixinOAuth2Keys 值
     */
    public void setWeixinOAuth2Keys(String weixinOAuth2Keys) {
        this.weixinOAuth2Keys = weixinOAuth2Keys;
    }

    /**
     * 获取weixinmpOAuth2Keys
     *
     * @return weixinmpOAuth2Keys
     */
    public String getWeixinmpOAuth2Keys() {
        return weixinmpOAuth2Keys;
    }

    /**
     * 设置weixinmpOAuth2Keys
     *
     * @param weixinmpOAuth2Keys 值
     */
    public void setWeixinmpOAuth2Keys(String weixinmpOAuth2Keys) {
        this.weixinmpOAuth2Keys = weixinmpOAuth2Keys;
    }

    /**
     * 获取qqOAuth2Keys
     *
     * @return qqOAuth2Keys
     */
    public String getQqOAuth2Keys() {
        return qqOAuth2Keys;
    }

    /**
     * 设置qqOAuth2Keys
     *
     * @param qqOAuth2Keys 值
     */
    public void setQqOAuth2Keys(String qqOAuth2Keys) {
        this.qqOAuth2Keys = qqOAuth2Keys;
    }

    /**
     * 获取sinaweiboOAuth2Keys
     *
     * @return sinaweiboOAuth2Keys
     */
    public String getSinaweiboOAuth2Keys() {
        return sinaweiboOAuth2Keys;
    }

    /**
     * 设置sinaweiboOAuth2Keys
     *
     * @param sinaweiboOAuth2Keys 值
     */
    public void setSinaweiboOAuth2Keys(String sinaweiboOAuth2Keys) {
        this.sinaweiboOAuth2Keys = sinaweiboOAuth2Keys;
    }

    /**
     * 获取weixinmpNoUnionId
     *
     * @return weixinmpNoUnionId
     */
    public String getWeixinmpNoUnionId() {
        return weixinmpNoUnionId;
    }

    /**
     * 设置weixinmpNoUnionId
     *
     * @param weixinmpNoUnionId 值
     */
    public void setWeixinmpNoUnionId(String weixinmpNoUnionId) {
        this.weixinmpNoUnionId = weixinmpNoUnionId;
    }

    /**
     * 获取anon
     *
     * @return anon
     */
    public List<String> getAnon() {
        return anon;
    }

    /**
     * 设置anon
     *
     * @param anon 值
     */
    public void setAnon(List<String> anon) {
        this.anon = anon;
    }
}
