package cn.faury.fdk.shiro.autoconfigure;

import cn.faury.fdk.common.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 配置文件
 */
@ConfigurationProperties(prefix = FdkShiroProperties.PROPERTIES_PREFIX)
public class FdkShiroProperties {
    /**
     * 配置文件前缀
     */
    public static final String PROPERTIES_PREFIX = "fdk.shiro";

    // 超时时间
    private int sessionTimeout = 1800000;

    // 系统编码或APP编码
    private String saCode="";

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
     * 获取saCode
     *
     * @return saCode
     */
    public String getSaCode() {
        return saCode;
    }

    /**
     * 设置saCode
     *
     * @param saCode 值
     */
    public void setSaCode(String saCode) {
        this.saCode = saCode;
    }

    @Override
    public String toString() {
        return JsonUtil.objectToJson(this);
    }
}
